package com.pdfmaster.esign.domain;

import java.time.Instant;
import java.util.Objects;

/** A single signer participating in a {@link SignatureRequest}. */
public record Signer(String email, int order, Instant signedAt) {

  public Signer {
    Objects.requireNonNull(email, "email");
    if (order < 0) {
      throw new IllegalArgumentException("order must be >= 0");
    }
  }
}
