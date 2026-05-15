package com.pdfmaster.pdfai.adapter.out.persistence;

import com.pdfmaster.pdfai.domain.JobStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job", schema = "pdf_ai")
public class JobEntity {

  /** Separator for the {@code input_keys} TEXT column. S3 keys cannot contain a newline. */
  static final String KEY_DELIMITER = "\n";

  @Id private UUID id;

  @Column(nullable = false, length = 64)
  private String op;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private JobStatus status;

  @Column(name = "input_keys", nullable = false, columnDefinition = "text")
  private String inputKeys;

  @Column(name = "output_key", length = 512)
  private String outputKey;

  @Column(name = "error_message", length = 2000)
  private String errorMessage;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected JobEntity() {
    // for JPA
  }

  public JobEntity(
      UUID id,
      String op,
      JobStatus status,
      List<String> inputKeys,
      String outputKey,
      String errorMessage,
      Instant createdAt,
      Instant updatedAt) {
    this.id = id;
    this.op = op;
    this.status = status;
    this.inputKeys = encodeKeys(inputKeys);
    this.outputKey = outputKey;
    this.errorMessage = errorMessage;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public String getOp() {
    return op;
  }

  public JobStatus getStatus() {
    return status;
  }

  public List<String> getInputKeys() {
    return decodeKeys(inputKeys);
  }

  public String getOutputKey() {
    return outputKey;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setStatus(JobStatus status) {
    this.status = status;
  }

  public void setOutputKey(String outputKey) {
    this.outputKey = outputKey;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  static String encodeKeys(List<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < keys.size(); i++) {
      String k = keys.get(i);
      if (k.indexOf('\n') >= 0) {
        throw new IllegalArgumentException("Input key may not contain a newline: " + k);
      }
      if (i > 0) {
        sb.append(KEY_DELIMITER);
      }
      sb.append(k);
    }
    return sb.toString();
  }

  static List<String> decodeKeys(String encoded) {
    if (encoded == null || encoded.isEmpty()) {
      return List.of();
    }
    List<String> result = new ArrayList<>();
    int start = 0;
    int idx;
    while ((idx = encoded.indexOf('\n', start)) >= 0) {
      result.add(encoded.substring(start, idx));
      start = idx + 1;
    }
    result.add(encoded.substring(start));
    return result;
  }
}
