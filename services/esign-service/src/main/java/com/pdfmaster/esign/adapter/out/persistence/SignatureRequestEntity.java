package com.pdfmaster.esign.adapter.out.persistence;

import com.pdfmaster.esign.domain.SignatureRequestStatus;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA record for {@code esign.signature_requests}. Stores signers as JSONB. */
@Entity
@Table(name = "signature_requests", schema = "esign")
public class SignatureRequestEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "sender_id", nullable = false)
  private UUID senderId;

  @Column(name = "document_s3_key", nullable = false, length = 512)
  private String documentS3Key;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 32)
  private SignatureRequestStatus status;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "signers", nullable = false, columnDefinition = "jsonb")
  private List<SignerData> signers = new ArrayList<>();

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected SignatureRequestEntity() {
    // JPA
  }

  public SignatureRequestEntity(
      UUID id,
      UUID senderId,
      String documentS3Key,
      SignatureRequestStatus status,
      List<SignerData> signers,
      Instant createdAt) {
    this.id = id;
    this.senderId = senderId;
    this.documentS3Key = documentS3Key;
    this.status = status;
    this.signers = new ArrayList<>(signers);
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getSenderId() {
    return senderId;
  }

  public String getDocumentS3Key() {
    return documentS3Key;
  }

  public SignatureRequestStatus getStatus() {
    return status;
  }

  public List<SignerData> getSigners() {
    return List.copyOf(signers);
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  /** Nested JSONB row shape; lives inside the entity to satisfy adapter-naming rules. */
  public record SignerData(String email, int order, Instant signedAt) {}
}
