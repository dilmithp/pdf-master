package com.pdfmaster.pdfocr.domain;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;

public record PresignedUpload(URI url, String key, Instant expiresAt) {

  public PresignedUpload {
    Objects.requireNonNull(url);
    Objects.requireNonNull(key);
    Objects.requireNonNull(expiresAt);
  }
}
