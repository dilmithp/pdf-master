package com.pdfmaster.pdfconvert.adapter.in;

import com.pdfmaster.pdfconvert.application.port.out.DocumentConverter;
import com.pdfmaster.pdfconvert.application.port.out.DocumentConverter.ConversionResult;
import com.pdfmaster.pdfconvert.application.port.out.JobRepository;
import com.pdfmaster.pdfconvert.application.port.out.ObjectStore;
import com.pdfmaster.pdfconvert.application.port.out.PdfImageConverter;
import com.pdfmaster.pdfconvert.config.RabbitMqConfig;
import com.pdfmaster.pdfconvert.domain.ConvertFormat;
import com.pdfmaster.pdfconvert.domain.JobId;
import com.pdfmaster.pdfconvert.domain.JobStatus;
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
 * Consumes {@code pdf.convert.requested}. Routes to the PDFBox image converter for image/text
 * paths, and to the LibreOffice runner for office-document paths.
 */
@Component
public class ConvertJobListener {

  private static final Logger LOG = LoggerFactory.getLogger(ConvertJobListener.class);

  /** First bytes that identify PDF, DOCX/XLSX/PPTX (ZIP), ODT (ZIP). */
  private static final byte[] PDF_MAGIC = "%PDF-".getBytes(StandardCharsets.US_ASCII);

  private static final byte[] ZIP_MAGIC = new byte[] {'P', 'K', 0x03, 0x04};

  /** PNG magic bytes: 8 bytes starting with 0x89 PNG. */
  private static final byte[] PNG_MAGIC = new byte[] {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};

  /** JPEG magic bytes: SOI marker FF D8 FF. */
  private static final byte[] JPG_MAGIC = new byte[] {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final DocumentConverter converter;
  private final PdfImageConverter imageConverter;
  private final Clock clock;

  public ConvertJobListener(
      ObjectStore objectStore,
      JobRepository jobRepository,
      DocumentConverter converter,
      PdfImageConverter imageConverter,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.converter = converter;
    this.imageConverter = imageConverter;
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
      fail(jobId, "Convert expects exactly one input key");
      throw new AmqpRejectAndDontRequeueException("Invalid convert payload: " + message);
    }
    try {
      ConvertFormat from = ConvertFormat.parse(String.valueOf(options.get("from")));
      ConvertFormat to = ConvertFormat.parse(String.valueOf(options.get("to")));
      jobRepository.markStatus(jobId, JobStatus.RUNNING);
      byte[] bytes = objectStore.download(keys.get(0));
      ensureKnownMagic(keys.get(0), bytes, from);
      ConversionResult result = doConvert(bytes, from, to);
      String outputKey = "results/" + jobId.value() + "/output." + extensionFor(to);
      objectStore.upload(
          outputKey,
          result.bytes(),
          result.contentType(),
          Map.of("auto-delete", "true", "job-id", jobId.value().toString()));
      jobRepository.save(
          jobRepository.findById(jobId).orElseThrow().withResult(outputKey, clock.instant()));
      LOG.info("Convert job {} succeeded ({} -> {})", jobId, from, to);
    } catch (RuntimeException ex) {
      LOG.error("Convert job {} failed", jobId, ex);
      fail(jobId, ex.getMessage());
      throw new AmqpRejectAndDontRequeueException("convert failed for job " + jobId, ex);
    }
  }

  private ConversionResult doConvert(byte[] bytes, ConvertFormat from, ConvertFormat to) {
    if (imageConverter.supports(from, to)) {
      return imageConverter.convert(bytes, from, to);
    }
    return converter.convert(bytes, from, to);
  }

  private static String extensionFor(ConvertFormat to) {
    // ZIP archive for multi-page image exports
    if (to == ConvertFormat.JPG || to == ConvertFormat.PNG) {
      return "zip";
    }
    return to.extension();
  }

  private static void ensureKnownMagic(String key, byte[] bytes, ConvertFormat from) {
    switch (from) {
      case PDF -> ensurePrefix(bytes, PDF_MAGIC, "PDF", key);
      case PNG -> ensurePrefix(bytes, PNG_MAGIC, "PNG", key);
      case JPG -> ensurePrefix(bytes, JPG_MAGIC, "JPEG", key);
      default ->
          // DOCX/XLSX/PPTX/ODT are all ZIP-based.
          ensurePrefix(bytes, ZIP_MAGIC, from.name() + " (zip)", key);
    }
  }

  private static void ensurePrefix(byte[] bytes, byte[] magic, String label, String key) {
    if (bytes.length < magic.length) {
      throw new IllegalArgumentException("Input too short to be " + label + ": " + key);
    }
    for (int i = 0; i < magic.length; i++) {
      if (bytes[i] != magic[i]) {
        throw new IllegalArgumentException("Magic-byte mismatch (" + label + "): " + key);
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
