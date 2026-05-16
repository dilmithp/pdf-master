package com.pdfmaster.pdfcore.application;

import com.pdfmaster.pdfcore.application.port.out.JobPublisher;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobRecord;
import com.pdfmaster.pdfcore.domain.JobStatus;
import com.pdfmaster.pdfcore.domain.PresignedUpload;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Application service for all single-input PDF-core operations (split, compress, rotate,
 * watermark, page-numbers, unlock, protect, extract-pages, reorder-pages, delete-pages, nup,
 * crop). Multi-input operations (merge) retain their dedicated service.
 */
public class CoreJobService {

  private static final Duration UPLOAD_TTL = Duration.ofMinutes(5);

  private static final Set<String> ALLOWED_OPS =
      Set.of(
          "split", "compress", "rotate", "watermark", "page-numbers", "unlock", "protect",
          "extract-pages", "reorder-pages", "delete-pages", "nup", "crop");

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final JobPublisher jobPublisher;
  private final Clock clock;

  public CoreJobService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.jobPublisher = jobPublisher;
    this.clock = clock;
  }

  public JobInitiation createJob(String op) {
    validateOp(op);
    JobId id = JobId.random();
    String key = "incoming/" + id.value() + "/source.pdf";
    PresignedUpload upload = objectStore.presignPut(key, "application/pdf", UPLOAD_TTL);
    JobRecord job = JobRecord.queued(id, op, List.of(upload.key()), clock.instant());
    jobRepository.save(job);
    return new JobInitiation(id, List.of(upload));
  }

  public JobRecord startJob(JobId id, String op, List<String> keys, Map<String, Object> options) {
    validateOp(op);
    JobRecord job =
        jobRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
    if (keys == null || keys.size() != 1) {
      throw new IllegalArgumentException("Exactly one input key required for op: " + op);
    }
    if (!job.inputKeys().contains(keys.get(0))) {
      throw new IllegalArgumentException("Key not authorised for this job: " + keys.get(0));
    }
    JobRecord queued = jobRepository.markStatus(id, JobStatus.QUEUED);
    jobPublisher.publish(op, id, keys, options == null ? Map.of() : options);
    return queued;
  }

  public JobRecord getJob(JobId id) {
    return jobRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
  }

  private static void validateOp(String op) {
    if (!ALLOWED_OPS.contains(op)) {
      throw new IllegalArgumentException("Unknown operation: " + op);
    }
  }
}
