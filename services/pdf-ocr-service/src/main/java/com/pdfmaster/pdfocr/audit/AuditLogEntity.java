package com.pdfmaster.pdfocr.audit;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "audit_log", schema = "pdf_ocr")
public class AuditLogEntity {

  @Id @Column(nullable = false, updatable = false) private UUID id;
  @Column(name = "user_id", length = 255) private String userId;
  @Column(name = "action", nullable = false, length = 64) private String action;
  @Column(name = "resource", length = 512) private String resource;
  @Column(name = "ip", length = 64) private String ip;
  @Column(name = "user_agent", length = 512) private String userAgent;
  @Column(name = "status_code") private Short statusCode;
  @Column(name = "error", columnDefinition = "text") private String error;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "metadata", columnDefinition = "jsonb") private JsonNode metadata;
  @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

  protected AuditLogEntity() {}

  public AuditLogEntity(UUID id, String userId, String action, String resource,
      String ip, String userAgent, Short statusCode, String error, JsonNode metadata, Instant createdAt) {
    this.id = id; this.userId = userId; this.action = action; this.resource = resource;
    this.ip = ip; this.userAgent = userAgent; this.statusCode = statusCode;
    this.error = error; this.metadata = metadata; this.createdAt = createdAt;
  }

  public UUID getId() { return id; }
  public String getUserId() { return userId; }
  public String getAction() { return action; }
  public String getResource() { return resource; }
  public String getIp() { return ip; }
  public String getUserAgent() { return userAgent; }
  public Short getStatusCode() { return statusCode; }
  public String getError() { return error; }
  public JsonNode getMetadata() { return metadata; }
  public Instant getCreatedAt() { return createdAt; }
}
