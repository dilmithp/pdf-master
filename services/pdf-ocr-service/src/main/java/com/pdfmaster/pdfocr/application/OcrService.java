package com.pdfmaster.pdfocr.application;

import com.pdfmaster.pdfocr.application.port.out.JobPublisher;
import com.pdfmaster.pdfocr.application.port.out.JobRepository;
import com.pdfmaster.pdfocr.application.port.out.ObjectStore;
import com.pdfmaster.pdfocr.domain.JobId;
import com.pdfmaster.pdfocr.domain.JobRecord;
import com.pdfmaster.pdfocr.domain.JobStatus;
import com.pdfmaster.pdfocr.domain.OcrLanguage;
import com.pdfmaster.pdfocr.domain.PresignedUpload;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Application service for OCR jobs. */
public class OcrService {

  public static final String OP = "ocr";
  private static final Duration UPLOAD_TTL = Duration.ofMinutes(5);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final JobPublisher jobPublisher;
  private final Clock clock;

  public OcrService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.jobPublisher = jobPublisher;
    this.clock = clock;
  }

  public JobInitiation createJob(String language) {
    // Validate the language up-front so callers can't allocate a job we'd later reject.
    OcrLanguage.normalise(language);
    JobId id = JobId.random();
    String key = "incoming/" + id.value() + "/source.pdf";
    PresignedUpload upload = objectStore.presignPut(key, "application/pdf", UPLOAD_TTL);
    JobRecord job = JobRecord.queued(id, OP, List.of(upload.key()), clock.instant());
    jobRepository.save(job);
    return new JobInitiation(id, List.of(upload));
  }

  public JobRecord startJob(
      JobId id, String language, List<String> keys, Map<String, Object> opts) {
    JobRecord job =
        jobRepository
            .findById(id)
            .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
    if (keys == null || keys.size() != 1) {
      throw new IllegalArgumentException("OCR expects exactly one input key");
    }
    if (!job.inputKeys().contains(keys.get(0))) {
      throw new IllegalArgumentException("Key not authorised for this job: " + keys.get(0));
    }
    JobRecord queued = jobRepository.markStatus(id, JobStatus.QUEUED);
    Map<String, Object> merged = new HashMap<>();
    if (opts != null) {
      merged.putAll(opts);
    }
    merged.put("language", OcrLanguage.normalise(language));
    jobPublisher.publish(OP, id, keys, merged);
    return queued;
  }

  public JobRecord getJob(JobId id) {
    return jobRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Unknown jobId: " + id));
  }
}
