package com.pdfmaster.billing.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Billing aggregate: a user's subscription state synchronized from Stripe. */
public record Subscription(
    UUID id,
    UUID userId,
    String stripeSubscriptionId,
    Plan plan,
    SubscriptionStatus status,
    Instant currentPeriodEnd) {

  public Subscription {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(userId, "userId");
    Objects.requireNonNull(stripeSubscriptionId, "stripeSubscriptionId");
    Objects.requireNonNull(plan, "plan");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(currentPeriodEnd, "currentPeriodEnd");
  }
}
