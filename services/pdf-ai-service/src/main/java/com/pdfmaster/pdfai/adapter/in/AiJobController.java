package com.pdfmaster.pdfai.adapter.in;

import com.pdfmaster.pdfai.application.AiService;
import com.pdfmaster.pdfai.application.JobInitiation;
import com.pdfmaster.pdfai.application.port.out.ObjectStore;
import com.pdfmaster.pdfai.domain.AiOperation;
import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.JobRecord;
import com.pdfmaster.pdfai.domain.PresignedUpload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

@RestController
@RequestMapping("/v1/jobs")
public class AiJobController {

  private static final Duration DOWNLOAD_TTL = Duration.ofMinutes(15);

  private final AiService aiService;
  private final ObjectStore objectStore;

  public AiJobController(AiService aiService, ObjectStore objectStore) {
    this.aiService = aiService;
    this.objectStore = objectStore;
  }

  @PostMapping("/ai")
  public ResponseEntity<CreateJobResponse> create(@Valid @RequestBody CreateAiJobRequest body) {
    AiOperation operation = AiOperation.parse(body.operation());
    JobInitiation init = aiService.createJob(operation);
    List<UploadUrl> urls = init.uploadUrls().stream().map(AiJobController::toUrl).toList();
    return ResponseEntity.accepted().body(new CreateJobResponse(init.jobId().value(), urls));
  }

  @PostMapping("/ai/{id}/start")
  public ResponseEntity<JobView> start(
      @PathVariable("id") UUID id, @Valid @RequestBody StartJobRequest body) {
    JobRecord job =
        aiService.startJob(
            new JobId(id), AiOperation.parse(body.operation()), body.keys(), body.options());
    return ResponseEntity.accepted().body(toView(job));
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobView> get(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(toView(aiService.getJob(new JobId(id))));
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

  public record CreateAiJobRequest(@NotBlank String operation) {}

  public record StartJobRequest(
      @NotBlank String operation, @NotEmpty List<String> keys, Map<String, Object> options) {}

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
