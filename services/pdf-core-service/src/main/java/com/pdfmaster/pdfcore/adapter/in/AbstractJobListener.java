package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobStatus;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

/**
 * Base logic shared across every PDF-core listener: payload parsing, magic-byte validation,
 * S3 upload with tagging, and job state transitions. Subclasses only implement {@link
 * #process(byte[], Map, JobId)}.
 */
abstract class AbstractJobListener {

  private final Logger log = LoggerFactory.getLogger(getClass());

  protected final ObjectStore objectStore;
  protected final JobRepository jobRepository;
  protected final Clock clock;

  protected AbstractJobListener(
      ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.clock = clock;
  }

  /**
   * Template method. Validates payload, downloads input, runs op, uploads result, marks job.
   * Expects exactly one input key unless the subclass overrides {@link #expectedInputCount()}.
   */
  protected final void handle(Map<String, Object> message) {
    JobId jobId = readJobId(message);
    @SuppressWarnings("unchecked")
    List<String> keys = (List<String>) message.get("inputKeys");
    @SuppressWarnings("unchecked")
    Map<String, Object> options =
        message.get("options") instanceof Map<?, ?> m
            ? (Map<String, Object>) m
            : Map.of();
    int expected = expectedInputCount();
    if (keys == null || keys.size() < expected) {
      fail(jobId, "Expected at least " + expected + " input key(s)");
      throw new AmqpRejectAndDontRequeueException("Invalid payload: " + message);
    }
    try {
      jobRepository.markStatus(jobId, JobStatus.RUNNING);
      byte[] input = objectStore.download(keys.get(0));
      PdfMagicValidator.validate(keys.get(0), input);
      ProcessResult result = process(input, options, jobId);
      String outputKey = "results/" + jobId.value() + "/" + result.filename();
      objectStore.upload(
          outputKey,
          result.bytes(),
          result.contentType(),
          Map.of("auto-delete", "true", "job-id", jobId.value().toString()));
      jobRepository.save(
          jobRepository.findById(jobId).orElseThrow().withResult(outputKey, clock.instant()));
      log.info("{} job {} succeeded -> {}", opName(), jobId, outputKey);
    } catch (RuntimeException | java.io.IOException ex) {
      log.error("{} job {} failed", opName(), jobId, ex);
      fail(jobId, ex.getMessage());
      throw new AmqpRejectAndDontRequeueException(opName() + " failed for job " + jobId, ex);
    }
  }

  /** Operation name used in log messages and exception text. */
  protected abstract String opName();

  /** Default = 1. Override for ops that take multiple inputs (e.g. merge takes ≥2). */
  protected int expectedInputCount() {
    return 1;
  }

  /**
   * Perform the actual PDF operation and return the result.
   *
   * @param input raw bytes of the (already validated) input PDF
   * @param options routing-key options map
   * @param jobId the current job id (for multi-output ops)
   */
  protected abstract ProcessResult process(
      byte[] input, Map<String, Object> options, JobId jobId) throws java.io.IOException;

  protected final void fail(JobId jobId, String message) {
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

  /** Carries the output bytes, content-type and target filename for the result S3 object. */
  record ProcessResult(byte[] bytes, String contentType, String filename) {}
}
