package com.pdfmaster.esign.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** E-signature request aggregate. Pure domain — no Spring/JPA dependencies. */
public record SignatureRequest(
    UUID id,
    UUID senderId,
    String documentS3Key,
    SignatureRequestStatus status,
    List<Signer> signers,
    Instant createdAt) {

  public SignatureRequest {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(senderId, "senderId");
    Objects.requireNonNull(documentS3Key, "documentS3Key");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(signers, "signers");
    Objects.requireNonNull(createdAt, "createdAt");
    signers = List.copyOf(signers);
  }
}
