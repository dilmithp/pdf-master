package com.pdfmaster.auth.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Auth-context user aggregate. Pure domain type — no Spring or JPA annotations. */
public record User(UUID id, String email, UserStatus status, Instant createdAt, Instant updatedAt) {

  public User {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(createdAt, "createdAt");
    Objects.requireNonNull(updatedAt, "updatedAt");
  }
}
