package com.pdfmaster.pdfconvert.domain;

import java.util.Objects;
import java.util.UUID;

/** Strongly-typed identifier for a worker job. */
public record JobId(UUID value) {

  public JobId {
    Objects.requireNonNull(value, "JobId.value must not be null");
  }

  public static JobId random() {
    return new JobId(UUID.randomUUID());
  }

  public static JobId of(String raw) {
    Objects.requireNonNull(raw, "raw");
    return new JobId(UUID.fromString(raw));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
