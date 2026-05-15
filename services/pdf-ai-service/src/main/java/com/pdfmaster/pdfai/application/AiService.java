package com.pdfmaster.pdfai.application;

import com.pdfmaster.pdfai.application.port.out.JobPublisher;
import com.pdfmaster.pdfai.application.port.out.JobRepository;
import com.pdfmaster.pdfai.application.port.out.ObjectStore;
import com.pdfmaster.pdfai.domain.AiOperation;
import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.JobRecord;
import com.pdfmaster.pdfai.domain.JobStatus;
import com.pdfmaster.pdfai.domain.PresignedUpload;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Application service for AI jobs. */
public class AiService {

  public static final String OP = "ai";
  private static final Duration UPLOAD_TTL = Duration.ofMinutes(5);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final JobPublisher jobPublisher;
  private final Clock clock;

  public AiService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.jobPublisher = jobPublisher;
    this.clock = clock;
  }

  public JobInitiation createJob(AiOperation operation) {
    JobId id = JobId.random();
    String key = "incoming/" + id.value() + "/source.pdf";
    PresignedUpload upload = objectStore.presignPut(key, "application/pdf", UPLOAD_TTL);
    JobRecord job =
        JobRecord.queued(
            id,
            OP + "." + operation.name().toLowerCase(java.util.Locale.ROOT),
            List.of(upload.key()),
            clock.instant());
    jobRepository.save(job);
    return new JobInitiation(id, List.of(upload));
  }

  public JobRecord startJob(
      JobId id, AiOperation operation, List<String> keys, Map<String, Object> opts) {
    JobRecord job =
        jobRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
    if (keys == null || keys.size() != 1) {
      throw new IllegalArgumentException("AI expects exactly one input key");
    }
    if (!job.inputKeys().contains(keys.get(0))) {
      throw new IllegalArgumentException("Key not authorised for this job: " + keys.get(0));
    }
    JobRecord queued = jobRepository.markStatus(id, JobStatus.QUEUED);
    Map<String, Object> merged = new HashMap<>();
    if (opts != null) {
      merged.putAll(opts);
    }
    merged.put("operation", operation.name());
    jobPublisher.publish(OP, id, keys, merged);
    return queued;
  }

  public JobRecord getJob(JobId id) {
    return jobRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
  }
}
