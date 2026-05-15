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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Application service that orchestrates the merge job lifecycle. */
public class MergeService {

  public static final String OP = "merge";
  private static final Duration UPLOAD_TTL = Duration.ofMinutes(5);
  private static final int MAX_FILES = 50;

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final JobPublisher jobPublisher;
  private final Clock clock;

  public MergeService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.jobPublisher = jobPublisher;
    this.clock = clock;
  }

  /** Allocates a job id and a presigned URL per input file. */
  public JobInitiation createJob(int fileCount) {
    if (fileCount < 2 || fileCount > MAX_FILES) {
      throw new IllegalArgumentException(
          "fileCount must be between 2 and " + MAX_FILES + ", got " + fileCount);
    }
    JobId id = JobId.random();
    List<PresignedUpload> uploads = new ArrayList<>(fileCount);
    for (int i = 0; i < fileCount; i++) {
      String key = "incoming/" + id.value() + "/" + i + ".pdf";
      uploads.add(objectStore.presignPut(key, "application/pdf", UPLOAD_TTL));
    }
    JobRecord job =
        JobRecord.queued(
            id, OP, uploads.stream().map(PresignedUpload::key).toList(), clock.instant());
    jobRepository.save(job);
    return new JobInitiation(id, uploads);
  }

  /** Validates inputs and publishes the merge request to RabbitMQ. */
  public JobRecord startJob(JobId id, List<String> keys, Map<String, Object> options) {
    JobRecord job =
        jobRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
    if (keys == null || keys.size() < 2) {
      throw new IllegalArgumentException("At least two input keys required to merge");
    }
    for (String k : keys) {
      if (!job.inputKeys().contains(k)) {
        throw new IllegalArgumentException("Key not authorised for this job: " + k);
      }
    }
    JobRecord queued = jobRepository.markStatus(id, JobStatus.QUEUED);
    jobPublisher.publish(OP, id, keys, options == null ? Map.of() : options);
    return queued;
  }

  public JobRecord getJob(JobId id) {
    return jobRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
  }
}
