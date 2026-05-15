package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@code pdf.merge.requested} messages. Validates every input via the PDF magic-byte
 * preamble before invoking PDFBox, runs the merge, uploads the result with {@code auto-delete=true}
 * tagging, and marks the job complete. Throws {@link AmqpRejectAndDontRequeueException} on failure
 * so the broker DLQs the message after marking the job FAILED.
 */
@Component
public class MergeJobListener {

  private static final Logger LOG = LoggerFactory.getLogger(MergeJobListener.class);
  private static final byte[] PDF_MAGIC = "%PDF-".getBytes(StandardCharsets.US_ASCII);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final Clock clock;

  public MergeJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.clock = clock;
  }

  @RabbitListener(queues = RabbitMqConfig.REQUEST_QUEUE)
  public void onMessage(Map<String, Object> message) {
    Objects.requireNonNull(message, "message");
    JobId jobId = readJobId(message);
    @SuppressWarnings("unchecked")
    List<String> keys = (List<String>) message.get("inputKeys");
    if (keys == null || keys.size() < 2) {
      fail(jobId, "At least two input keys required");
      throw new AmqpRejectAndDontRequeueException("Invalid merge payload: " + message);
    }
    try {
      jobRepository.markStatus(jobId, JobStatus.RUNNING);
      byte[] merged = mergeAll(keys);
      String outputKey = "results/" + jobId.value() + "/merged.pdf";
      objectStore.upload(
          outputKey,
          merged,
          "application/pdf",
          Map.of("auto-delete", "true", "job-id", jobId.value().toString()));
      jobRepository.save(
          jobRepository.findById(jobId).orElseThrow().withResult(outputKey, clock.instant()));
      LOG.info("Merge job {} succeeded ({} inputs -> {})", jobId, keys.size(), outputKey);
    } catch (IOException | RuntimeException ex) {
      LOG.error("Merge job {} failed", jobId, ex);
      fail(jobId, ex.getMessage());
      throw new AmqpRejectAndDontRequeueException("merge failed for job " + jobId, ex);
    }
  }

  /** Validates every input by magic-byte and merges via PDFBox. */
  byte[] mergeAll(List<String> keys) throws IOException {
    PDFMergerUtility merger = new PDFMergerUtility();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    merger.setDestinationStream(out);
    for (String key : keys) {
      byte[] bytes = objectStore.download(key);
      ensurePdfMagic(key, bytes);
      // Defensive double-parse: confirm PDFBox can open it before merging, fail loudly otherwise.
      try (PDDocument doc =
          Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(bytes)))) {
        if (doc.getNumberOfPages() == 0) {
          throw new IOException("Empty PDF at key " + key);
        }
      }
      merger.addSource(new RandomAccessReadBuffer(new ByteArrayInputStream(bytes)));
    }
    merger.mergeDocuments(org.apache.pdfbox.io.IOUtils.createMemoryOnlyStreamCache());
    return out.toByteArray();
  }

  private static void ensurePdfMagic(String key, byte[] bytes) {
    if (bytes.length < PDF_MAGIC.length) {
      throw new IllegalArgumentException("Input too short to be a PDF: " + key);
    }
    for (int i = 0; i < PDF_MAGIC.length; i++) {
      if (bytes[i] != PDF_MAGIC[i]) {
        throw new IllegalArgumentException("Input is not a PDF (magic-byte mismatch): " + key);
      }
    }
  }

  private void fail(JobId jobId, String message) {
    jobRepository
        .findById(jobId)
        .ifPresent(
            j ->
                jobRepository.save(
                    j.withFailure(message == null ? "unknown" : message, clock.instant())));
  }

  private static JobId readJobId(Map<String, Object> message) {
    Object raw = message.get("jobId");
    if (raw == null) {
      throw new AmqpRejectAndDontRequeueException("Missing jobId in payload");
    }
    return new JobId(UUID.fromString(raw.toString()));
  }
}
