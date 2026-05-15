package com.pdfmaster.pdfcore.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable snapshot of a worker job. Mutated copies are produced by {@link #withStatus} and {@link
 * #withResult}.
 */
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
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(op, "op");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(inputKeys, "inputKeys");
    Objects.requireNonNull(outputKey, "outputKey");
    Objects.requireNonNull(error, "error");
    Objects.requireNonNull(createdAt, "createdAt");
    Objects.requireNonNull(updatedAt, "updatedAt");
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
