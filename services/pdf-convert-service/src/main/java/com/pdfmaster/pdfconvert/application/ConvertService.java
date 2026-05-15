package com.pdfmaster.pdfconvert.application;

import com.pdfmaster.pdfconvert.application.port.out.JobPublisher;
import com.pdfmaster.pdfconvert.application.port.out.JobRepository;
import com.pdfmaster.pdfconvert.application.port.out.ObjectStore;
import com.pdfmaster.pdfconvert.domain.ConvertFormat;
import com.pdfmaster.pdfconvert.domain.JobId;
import com.pdfmaster.pdfconvert.domain.JobRecord;
import com.pdfmaster.pdfconvert.domain.JobStatus;
import com.pdfmaster.pdfconvert.domain.PresignedUpload;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Application service orchestrating conversion jobs. */
public class ConvertService {

  public static final String OP = "convert";
  private static final Duration UPLOAD_TTL = Duration.ofMinutes(5);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final JobPublisher jobPublisher;
  private final Clock clock;

  public ConvertService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.jobPublisher = jobPublisher;
    this.clock = clock;
  }

  public JobInitiation createJob(ConvertFormat from, ConvertFormat to) {
    if (from == to) {
      throw new IllegalArgumentException("Source and target format must differ");
    }
    JobId id = JobId.random();
    String key = "incoming/" + id.value() + "/source." + from.extension();
    PresignedUpload upload = objectStore.presignPut(key, from.contentType(), UPLOAD_TTL);
    JobRecord job = JobRecord.queued(id, OP, List.of(upload.key()), clock.instant());
    jobRepository.save(job);
    return new JobInitiation(id, List.of(upload));
  }

  public JobRecord startJob(
      JobId id, ConvertFormat from, ConvertFormat to, List<String> keys, Map<String, Object> opts) {
    JobRecord job =
        jobRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
    if (keys == null || keys.size() != 1) {
      throw new IllegalArgumentException("Convert expects exactly one input key");
    }
    if (!job.inputKeys().contains(keys.get(0))) {
      throw new IllegalArgumentException("Key not authorised for this job: " + keys.get(0));
    }
    JobRecord queued = jobRepository.markStatus(id, JobStatus.QUEUED);
    Map<String, Object> merged = new HashMap<>();
    if (opts != null) {
      merged.putAll(opts);
    }
    merged.put("from", from.name());
    merged.put("to", to.name());
    jobPublisher.publish(OP, id, keys, merged);
    return queued;
  }

  public JobRecord getJob(JobId id) {
    return jobRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
  }
}
