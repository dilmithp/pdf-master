package com.pdfmaster.billing.application;

/** Outbound port for persisting deduplicated Stripe webhook events. */
public interface StripeEventRepository {

  /** Returns {@code true} if the event id was not previously seen and was successfully recorded. */
  boolean recordIfNew(String eventId, String eventType, String rawPayload);
}
