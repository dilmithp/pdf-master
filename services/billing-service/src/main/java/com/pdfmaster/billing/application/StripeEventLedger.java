package com.pdfmaster.billing.application;

import com.stripe.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Idempotency ledger for incoming Stripe webhook events. Delegates persistence to the
 * {@link StripeEventRepository} outbound port so this class has no adapter dependencies.
 *
 * <p>Returns {@code true} for a genuinely new event and {@code false} for a duplicate delivery
 * (including concurrent race conditions caught by the PK constraint in the adapter).
 */
@Service
public class StripeEventLedger {

  private static final Logger log = LoggerFactory.getLogger(StripeEventLedger.class);

  private final StripeEventRepository repository;

  public StripeEventLedger(StripeEventRepository repository) {
    this.repository = repository;
  }

  /**
   * Attempts to record the event. Returns {@code true} if newly inserted, {@code false} if already
   * seen (idempotent replay or concurrent duplicate).
   *
   * @param event      the verified Stripe event object
   * @param rawPayload the raw JSON string from the HTTP request body
   */
  public boolean recordIfNew(Event event, String rawPayload) {
    boolean fresh = repository.recordIfNew(event.getId(), event.getType(), rawPayload);
    if (!fresh) {
      log.debug("Duplicate Stripe event suppressed id={}", event.getId());
    }
    return fresh;
  }
}
