package com.pdfmaster.pdfconvert.domain;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;

/** A presigned PUT URL produced by the object store. */
public record PresignedUpload(URI url, String key, Instant expiresAt) {

  public PresignedUpload {
    Objects.requireNonNull(url, "url");
    Objects.requireNonNull(key, "key");
    Objects.requireNonNull(expiresAt, "expiresAt");
  }
}
