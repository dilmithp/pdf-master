package com.pdfmaster.pdfai.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record JobRecord(
    JobId id,
    String op,
    JobStatus status,
    List<String> inputKeys,
    Optional<String> outputKey,
    Optional<String> error,
    Instant createdAt,
    Instant updatedAt) {

  public JobRecord {
    Objects.requireNonNull(id);
    Objects.requireNonNull(op);
    Objects.requireNonNull(status);
    Objects.requireNonNull(inputKeys);
    Objects.requireNonNull(outputKey);
    Objects.requireNonNull(error);
    Objects.requireNonNull(createdAt);
    Objects.requireNonNull(updatedAt);
    inputKeys = List.copyOf(inputKeys);
  }

  public static JobRecord queued(JobId id, String op, List<String> inputKeys, Instant now) {
    return new JobRecord(
        id, op, JobStatus.QUEUED, inputKeys, Optional.empty(), Optional.empty(), now, now);
  }

  public JobRecord withStatus(JobStatus next, Instant now) {
    return new JobRecord(id, op, next, inputKeys, outputKey, error, createdAt, now);
  }

  public JobRecord withResult(String outputKey, Instant now) {
    return new JobRecord(
        id,
        op,
        JobStatus.SUCCEEDED,
        inputKeys,
        Optional.of(outputKey),
        Optional.empty(),
        createdAt,
        now);
  }

  public JobRecord withFailure(String error, Instant now) {
    return new JobRecord(
        id, op, JobStatus.FAILED, inputKeys, outputKey, Optional.of(error), createdAt, now);
  }
}
