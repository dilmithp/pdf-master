package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.JobInitiation;
import com.pdfmaster.pdfcore.application.MergeService;
import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobRecord;
import com.pdfmaster.pdfcore.domain.PresignedUpload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP surface for the merge operation. */
@RestController
@RequestMapping("/v1/jobs")
public class MergeJobController {

  private static final Duration DOWNLOAD_TTL = Duration.ofMinutes(15);

  private final MergeService mergeService;
  private final com.pdfmaster.pdfcore.application.port.out.ObjectStore objectStore;

  public MergeJobController(
      MergeService mergeService,
      com.pdfmaster.pdfcore.application.port.out.ObjectStore objectStore) {
    this.mergeService = mergeService;
    this.objectStore = objectStore;
  }

  @PostMapping("/merge")
  public ResponseEntity<CreateJobResponse> create(@Valid @RequestBody CreateMergeJobRequest body) {
    JobInitiation init = mergeService.createJob(body.fileCount());
    List<UploadUrl> urls = init.uploadUrls().stream().map(MergeJobController::toUrl).toList();
    return ResponseEntity.accepted().body(new CreateJobResponse(init.jobId().value(), urls));
  }

  @PostMapping("/merge/{id}/start")
  public ResponseEntity<JobView> start(
      @PathVariable("id") UUID id, @Valid @RequestBody StartJobRequest body) {
    JobRecord job = mergeService.startJob(new JobId(id), body.keys(), body.options());
    return ResponseEntity.accepted().body(toView(job));
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobView> get(@PathVariable("id") UUID id) {
    JobRecord job = mergeService.getJob(new JobId(id));
    return ResponseEntity.ok(toView(job));
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

  public record CreateMergeJobRequest(@Min(2) @Max(50) int fileCount) {}

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
