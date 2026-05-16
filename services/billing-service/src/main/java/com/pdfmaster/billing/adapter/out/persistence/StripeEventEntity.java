package com.pdfmaster.billing.adapter.out.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** JPA record for {@code billing.stripe_event}. The {@code id} column is the Stripe event id. */
@Entity
@Table(name = "stripe_event", schema = "billing")
public class StripeEventEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false, length = 128)
  private String id;

  @Column(name = "event_type", nullable = false, length = 128)
  private String eventType;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
  private JsonNode payload;

  @Column(name = "received_at", nullable = false, updatable = false)
  private Instant receivedAt;

  protected StripeEventEntity() {
    // JPA
  }

  public StripeEventEntity(String id, String eventType, JsonNode payload, Instant receivedAt) {
    this.id = id;
    this.eventType = eventType;
    this.payload = payload;
    this.receivedAt = receivedAt;
  }

  public String getId() {
    return id;
  }

  public String getEventType() {
    return eventType;
  }

  public JsonNode getPayload() {
    return payload;
  }

  public Instant getReceivedAt() {
    return receivedAt;
  }
}
