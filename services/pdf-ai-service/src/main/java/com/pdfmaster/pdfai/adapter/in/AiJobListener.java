package com.pdfmaster.pdfai.adapter.in;

import com.pdfmaster.pdfai.application.port.out.JobRepository;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.application.port.out.LlmClient.LlmResponse;
import com.pdfmaster.pdfai.application.port.out.ObjectStore;
import com.pdfmaster.pdfai.config.RabbitMqConfig;
import com.pdfmaster.pdfai.domain.AiOperation;
import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.JobStatus;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@code pdf.ai.requested}. Validates PDF magic-bytes, calls the {@link LlmClient}, and
 * writes the response to S3. Real embedding indexing into pgvector is left for a follow-up — the
 * persistence adapter is wired so it can be enabled without further plumbing.
 */
@Component
public class AiJobListener {

  private static final Logger LOG = LoggerFactory.getLogger(AiJobListener.class);
  private static final byte[] PDF_MAGIC = "%PDF-".getBytes(StandardCharsets.US_ASCII);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final LlmClient llmClient;
  private final Clock clock;

  public AiJobListener(
      ObjectStore objectStore, JobRepository jobRepository, LlmClient llmClient, Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.llmClient = llmClient;
    this.clock = clock;
  }

  @RabbitListener(queues = RabbitMqConfig.REQUEST_QUEUE)
  public void onMessage(Map<String, Object> message) {
    Objects.requireNonNull(message, "message");
    JobId jobId = readJobId(message);
    @SuppressWarnings("unchecked")
    List<String> keys = (List<String>) message.get("inputKeys");
    @SuppressWarnings("unchecked")
    Map<String, Object> options = (Map<String, Object>) message.getOrDefault("options", Map.of());
    if (keys == null || keys.size() != 1) {
      fail(jobId, "AI expects exactly one input key");
      throw new AmqpRejectAndDontRequeueException("Invalid AI payload: " + message);
    }
    try {
      AiOperation operation = AiOperation.parse(String.valueOf(options.get("operation")));
      jobRepository.markStatus(jobId, JobStatus.RUNNING);
      byte[] pdf = objectStore.download(keys.get(0));
      ensurePdfMagic(keys.get(0), pdf);
      // For the scaffold we pass a short fingerprint of the input rather than full text extraction
      // — the extraction step will land with the AI feature ticket.
      String prompt = "doc:" + jobId + ":bytes=" + pdf.length;
      LlmResponse response = llmClient.complete(operation, prompt);
      byte[] outBytes = response.text().getBytes(StandardCharsets.UTF_8);
      String outputKey = "results/" + jobId.value() + "/response.txt";
      objectStore.upload(
          outputKey,
          outBytes,
          "text/plain; charset=utf-8",
          Map.of("auto-delete", "true", "job-id", jobId.value().toString()));
      jobRepository.save(
          jobRepository.findById(jobId).orElseThrow().withResult(outputKey, clock.instant()));
      LOG.info(
          "AI job {} succeeded (op={}, tokens_out={})", jobId, operation, response.tokensOut());
    } catch (RuntimeException ex) {
      LOG.error("AI job {} failed", jobId, ex);
      fail(jobId, ex.getMessage());
      throw new AmqpRejectAndDontRequeueException("ai failed for job " + jobId, ex);
    }
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
