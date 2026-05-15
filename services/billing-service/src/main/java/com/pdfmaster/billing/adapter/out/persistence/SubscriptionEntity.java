package com.pdfmaster.billing.adapter.out.persistence;

import com.pdfmaster.billing.domain.Plan;
import com.pdfmaster.billing.domain.SubscriptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA record for {@code billing.subscriptions}. */
@Entity
@Table(name = "subscriptions", schema = "billing")
public class SubscriptionEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "stripe_subscription_id", nullable = false, length = 64, unique = true)
  private String stripeSubscriptionId;

  @Enumerated(EnumType.STRING)
  @Column(name = "plan", nullable = false, length = 32)
  private Plan plan;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 32)
  private SubscriptionStatus status;

  @Column(name = "current_period_end", nullable = false)
  private Instant currentPeriodEnd;

  protected SubscriptionEntity() {
    // JPA
  }

  public SubscriptionEntity(
      UUID id,
      UUID userId,
      String stripeSubscriptionId,
      Plan plan,
      SubscriptionStatus status,
      Instant currentPeriodEnd) {
    this.id = id;
    this.userId = userId;
    this.stripeSubscriptionId = stripeSubscriptionId;
    this.plan = plan;
    this.status = status;
    this.currentPeriodEnd = currentPeriodEnd;
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getStripeSubscriptionId() {
    return stripeSubscriptionId;
  }

  public Plan getPlan() {
    return plan;
  }

  public SubscriptionStatus getStatus() {
    return status;
  }

  public Instant getCurrentPeriodEnd() {
    return currentPeriodEnd;
  }
}
