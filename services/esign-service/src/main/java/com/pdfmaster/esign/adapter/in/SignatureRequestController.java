package com.pdfmaster.esign.adapter.in;

import com.pdfmaster.esign.application.SignatureRequestService;
import com.pdfmaster.esign.domain.SignatureRequest;
import com.pdfmaster.esign.domain.Signer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** REST adapter for signature request creation and lookup. */
@RestController
@RequestMapping("/v1/signature-requests")
public class SignatureRequestController {

  private final SignatureRequestService service;

  public SignatureRequestController(SignatureRequestService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<SignatureRequestResponse> create(
      @Valid @RequestBody CreateRequest request) {
    List<Signer> signers =
        request.signers().stream().map(s -> new Signer(s.email(), s.order(), null)).toList();
    SignatureRequest created = service.create(request.senderId(), request.documentS3Key(), signers);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
    return ResponseEntity.created(location).body(SignatureRequestResponse.from(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SignatureRequestResponse> get(@PathVariable UUID id) {
    return service
        .findById(id)
        .map(SignatureRequestResponse::from)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  public record CreateRequest(
      @NotNull UUID senderId,
      @NotBlank @Size(max = 512) String documentS3Key,
      @NotEmpty @Valid List<SignerInput> signers) {}

  public record SignerInput(@Email @NotBlank String email, @PositiveOrZero int order) {}

  public record SignatureRequestResponse(
      UUID id,
      UUID senderId,
      String documentS3Key,
      String status,
      List<SignerOutput> signers,
      Instant createdAt) {

    static SignatureRequestResponse from(SignatureRequest r) {
      return new SignatureRequestResponse(
          r.id(),
          r.senderId(),
          r.documentS3Key(),
          r.status().name(),
          r.signers().stream()
              .map(s -> new SignerOutput(s.email(), s.order(), s.signedAt()))
              .toList(),
          r.createdAt());
    }
  }

  public record SignerOutput(String email, int order, Instant signedAt) {}
}
