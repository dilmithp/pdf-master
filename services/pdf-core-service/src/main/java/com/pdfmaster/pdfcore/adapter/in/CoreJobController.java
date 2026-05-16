package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.CoreJobService;
import com.pdfmaster.pdfcore.application.JobInitiation;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobRecord;
import com.pdfmaster.pdfcore.domain.PresignedUpload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Generic controller that handles create/start/status for all single-input PDF-core operations
 * (split, compress, rotate, watermark, page-numbers, unlock, protect, extract-pages, reorder-pages,
 * delete-pages, nup, crop).
 *
 * <p>The {@code op} path segment identifies the operation and becomes the RabbitMQ routing key.
 */
@RestController
@RequestMapping("/v1/jobs")
public class CoreJobController {

  private static final Duration DOWNLOAD_TTL = Duration.ofMinutes(15);

  private final CoreJobService coreJobService;
  private final ObjectStore objectStore;

  public CoreJobController(CoreJobService coreJobService, ObjectStore objectStore) {
    this.coreJobService = coreJobService;
    this.objectStore = objectStore;
  }

  @PostMapping("/{op}")
  public ResponseEntity<CreateJobResponse> create(
      @PathVariable("op") String op, @Valid @RequestBody CreateCoreJobRequest body) {
    JobInitiation init = coreJobService.createJob(op);
    List<UploadUrl> urls = init.uploadUrls().stream().map(CoreJobController::toUrl).toList();
    return ResponseEntity.accepted().body(new CreateJobResponse(init.jobId().value(), urls));
  }

  @PostMapping("/{op}/{id}/start")
  public ResponseEntity<JobView> start(
      @PathVariable("op") String op,
      @PathVariable("id") UUID id,
      @Valid @RequestBody StartJobRequest body) {
    JobRecord job =
        coreJobService.startJob(new JobId(id), op, body.keys(), body.options());
    return ResponseEntity.accepted().body(toView(job));
  }

  private JobView toView(JobRecord job) {
    URI downloadUrl =
        job.outputKey().map(k -> objectStore.presignGet(k, DOWNLOAD_TTL)).orElse(null);
    return new JobView(
        job.id().value(),
        job.op(),
        job.status().name(),
        job.inputKeys(),
        downloadUrl,
        job.error().orElse(null),
        job.createdAt(),
        job.updatedAt());
  }

  private static UploadUrl toUrl(PresignedUpload u) {
    return new UploadUrl(u.url(), u.key(), u.expiresAt());
  }

  // ---- request / response records ----

  public record CreateCoreJobRequest(Map<String, Object> options) {}

  public record StartJobRequest(@NotEmpty List<String> keys, Map<String, Object> options) {}

  public record CreateJobResponse(UUID jobId, List<UploadUrl> uploadUrls) {}

  public record UploadUrl(URI url, String key, Instant expiresAt) {}

  public record JobView(
      UUID jobId,
      String op,
      String status,
      List<String> inputKeys,
      URI downloadUrl,
      String error,
      Instant createdAt,
      Instant updatedAt) {}
}
