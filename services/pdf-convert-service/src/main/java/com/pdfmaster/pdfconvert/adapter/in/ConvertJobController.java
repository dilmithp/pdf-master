package com.pdfmaster.pdfconvert.adapter.in;

import com.pdfmaster.pdfconvert.application.ConvertService;
import com.pdfmaster.pdfconvert.application.JobInitiation;
import com.pdfmaster.pdfconvert.application.port.out.ObjectStore;
import com.pdfmaster.pdfconvert.domain.ConvertFormat;
import com.pdfmaster.pdfconvert.domain.JobId;
import com.pdfmaster.pdfconvert.domain.JobRecord;
import com.pdfmaster.pdfconvert.domain.PresignedUpload;
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
public class ConvertJobController {

  private static final Duration DOWNLOAD_TTL = Duration.ofMinutes(15);

  private final ConvertService convertService;
  private final ObjectStore objectStore;

  public ConvertJobController(ConvertService convertService, ObjectStore objectStore) {
    this.convertService = convertService;
    this.objectStore = objectStore;
  }

  @PostMapping("/convert")
  public ResponseEntity<CreateJobResponse> create(
      @Valid @RequestBody CreateConvertJobRequest body) {
    ConvertFormat from = ConvertFormat.parse(body.from());
    ConvertFormat to = ConvertFormat.parse(body.to());
    JobInitiation init = convertService.createJob(from, to);
    List<UploadUrl> urls = init.uploadUrls().stream().map(ConvertJobController::toUrl).toList();
    return ResponseEntity.accepted().body(new CreateJobResponse(init.jobId().value(), urls));
  }

  @PostMapping("/convert/{id}/start")
  public ResponseEntity<JobView> start(
      @PathVariable("id") UUID id, @Valid @RequestBody StartJobRequest body) {
    ConvertFormat from = ConvertFormat.parse(body.from());
    ConvertFormat to = ConvertFormat.parse(body.to());
    JobRecord job = convertService.startJob(new JobId(id), from, to, body.keys(), body.options());
    return ResponseEntity.accepted().body(toView(job));
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobView> get(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(toView(convertService.getJob(new JobId(id))));
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

  public record CreateConvertJobRequest(@NotBlank String from, @NotBlank String to) {}

  public record StartJobRequest(
      @NotBlank String from,
      @NotBlank String to,
      @NotEmpty List<String> keys,
      Map<String, Object> options) {}

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
