package com.pdfmaster.billing.domain;

/** Mirror of the Stripe subscription lifecycle (subset). */
public enum SubscriptionStatus {
  TRIALING,
  ACTIVE,
  PAST_DUE,
  CANCELED,
  UNPAID,
  INCOMPLETE,
  INCOMPLETE_EXPIRED,
  PAUSED
}
