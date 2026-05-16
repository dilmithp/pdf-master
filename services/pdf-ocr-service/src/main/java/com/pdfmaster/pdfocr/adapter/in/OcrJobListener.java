package com.pdfmaster.pdfocr.adapter.in;

import com.pdfmaster.pdfocr.application.port.out.JobRepository;
import com.pdfmaster.pdfocr.application.port.out.ObjectStore;
import com.pdfmaster.pdfocr.application.port.out.OcrEngine;
import com.pdfmaster.pdfocr.config.RabbitMqConfig;
import com.pdfmaster.pdfocr.domain.JobId;
import com.pdfmaster.pdfocr.domain.JobStatus;
import com.pdfmaster.pdfocr.domain.OcrLanguage;
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
 * Consumes {@code pdf.ocr.requested}. Validates PDF magic-bytes, runs OCR via the configured
 * {@link OcrEngine} (Noop in tests, Tesseract in production), uploads the result.
 *
 * <p>Output mode is controlled by the {@code outputMode} option:
 * <ul>
 *   <li>{@code text} (default) — plain text file ({@code text.txt})</li>
 *   <li>{@code json} — JSON with per-page text ({@code result.json})</li>
 *   <li>{@code searchable-pdf} — original PDF with invisible text overlay ({@code searchable.pdf})</li>
 * </ul>
 */
@Component
public class OcrJobListener {

  private static final Logger LOG = LoggerFactory.getLogger(OcrJobListener.class);
  private static final byte[] PDF_MAGIC = "%PDF-".getBytes(StandardCharsets.US_ASCII);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final OcrEngine ocrEngine;
  private final Clock clock;

  public OcrJobListener(
      ObjectStore objectStore, JobRepository jobRepository, OcrEngine ocrEngine, Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.ocrEngine = ocrEngine;
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
      fail(jobId, "OCR expects exactly one input key");
      throw new AmqpRejectAndDontRequeueException("Invalid OCR payload: " + message);
    }
    try {
      String language = OcrLanguage.normalise(String.valueOf(options.get("language")));
      String outputMode = options.get("outputMode") instanceof String s ? s : "text";
      jobRepository.markStatus(jobId, JobStatus.RUNNING);
      byte[] pdf = objectStore.download(keys.get(0));
      ensurePdfMagic(keys.get(0), pdf);

      OcrResult result = runOcr(pdf, language, outputMode, jobId);

      objectStore.upload(
          result.outputKey(),
          result.bytes(),
          result.contentType(),
          Map.of("auto-delete", "true", "job-id", jobId.value().toString()));
      jobRepository.save(
          jobRepository.findById(jobId).orElseThrow().withResult(result.outputKey(), clock.instant()));
      LOG.info("OCR job {} succeeded (mode={}, bytes={})", jobId, outputMode, result.bytes().length);
    } catch (RuntimeException ex) {
      LOG.error("OCR job {} failed", jobId, ex);
      fail(jobId, ex.getMessage());
      throw new AmqpRejectAndDontRequeueException("ocr failed for job " + jobId, ex);
    }
  }

  private OcrResult runOcr(byte[] pdf, String language, String outputMode, JobId jobId) {
    return switch (outputMode) {
      case "searchable-pdf" -> {
        byte[] result = ocrEngine.createSearchablePdf(pdf, language);
        yield new OcrResult(
            "results/" + jobId.value() + "/searchable.pdf",
            result,
            "application/pdf");
      }
      case "json" -> {
        List<OcrEngine.PageResult> pages = ocrEngine.extractWithBounds(pdf, language);
        String json = toJson(pages);
        yield new OcrResult(
            "results/" + jobId.value() + "/result.json",
            json.getBytes(StandardCharsets.UTF_8),
            "application/json");
      }
      default -> {
        String text = ocrEngine.extractText(pdf, language);
        yield new OcrResult(
            "results/" + jobId.value() + "/text.txt",
            text.getBytes(StandardCharsets.UTF_8),
            "text/plain; charset=utf-8");
      }
    };
  }

  private static String toJson(List<OcrEngine.PageResult> pages) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < pages.size(); i++) {
      OcrEngine.PageResult page = pages.get(i);
      if (i > 0) sb.append(',');
      sb.append("{\"page\":")
          .append(page.pageNumber())
          .append(",\"text\":")
          .append(jsonString(page.text()))
          .append(",\"words\":[");
      List<OcrEngine.WordBox> words = page.words();
      for (int j = 0; j < words.size(); j++) {
        OcrEngine.WordBox w = words.get(j);
        if (j > 0) sb.append(',');
        sb.append("{\"word\":").append(jsonString(w.word()))
            .append(",\"x\":").append(w.x())
            .append(",\"y\":").append(w.y())
            .append(",\"w\":").append(w.width())
            .append(",\"h\":").append(w.height())
            .append('}');
      }
      sb.append("]}");
    }
    sb.append(']');
    return sb.toString();
  }

  private static String jsonString(String s) {
    if (s == null) return "null";
    return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
        .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
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

  private record OcrResult(String outputKey, byte[] bytes, String contentType) {}
}
