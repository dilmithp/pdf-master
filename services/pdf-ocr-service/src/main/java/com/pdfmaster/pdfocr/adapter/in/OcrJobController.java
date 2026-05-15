package com.pdfmaster.pdfocr.adapter.in;

import com.pdfmaster.pdfocr.application.JobInitiation;
import com.pdfmaster.pdfocr.application.OcrService;
import com.pdfmaster.pdfocr.application.port.out.ObjectStore;
import com.pdfmaster.pdfocr.domain.JobId;
import com.pdfmaster.pdfocr.domain.JobRecord;
import com.pdfmaster.pdfocr.domain.PresignedUpload;
import jakarta.validation.Valid;
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
public class OcrJobController {

  private static final Duration DOWNLOAD_TTL = Duration.ofMinutes(15);

  private final OcrService ocrService;
  private final ObjectStore objectStore;

  public OcrJobController(OcrService ocrService, ObjectStore objectStore) {
    this.ocrService = ocrService;
    this.objectStore = objectStore;
  }

  @PostMapping("/ocr")
  public ResponseEntity<CreateJobResponse> create(@Valid @RequestBody CreateOcrJobRequest body) {
    JobInitiation init = ocrService.createJob(body.language());
    List<UploadUrl> urls = init.uploadUrls().stream().map(OcrJobController::toUrl).toList();
    return ResponseEntity.accepted().body(new CreateJobResponse(init.jobId().value(), urls));
  }

  @PostMapping("/ocr/{id}/start")
  public ResponseEntity<JobView> start(
      @PathVariable("id") UUID id, @Valid @RequestBody StartJobRequest body) {
    JobRecord job =
        ocrService.startJob(new JobId(id), body.language(), body.keys(), body.options());
    return ResponseEntity.accepted().body(toView(job));
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobView> get(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(toView(ocrService.getJob(new JobId(id))));
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

  public record CreateOcrJobRequest(String language) {}

  public record StartJobRequest(
      String language, @NotEmpty List<String> keys, Map<String, Object> options) {}

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
