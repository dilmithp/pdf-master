package com.pdfmaster.billing.adapter.in;

import com.pdfmaster.billing.application.StripeEventLedger;
import com.pdfmaster.billing.config.StripeProperties;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Receives Stripe webhook callbacks, verifies HMAC, and deduplicates via the event ledger. */
@RestController
@RequestMapping("/v1/webhooks/stripe")
public class StripeWebhookController {

  private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

  private final StripeProperties properties;
  private final StripeEventLedger ledger;

  public StripeWebhookController(StripeProperties properties, StripeEventLedger ledger) {
    this.properties = properties;
    this.ledger = ledger;
  }

  @PostMapping
  public ResponseEntity<Void> receive(
      @RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
    Event event;
    try {
      event = Webhook.constructEvent(payload, signature, properties.webhookSecret());
    } catch (SignatureVerificationException e) {
      log.warn("Rejected Stripe webhook: invalid signature");
      return ResponseEntity.badRequest().build();
    }
    boolean fresh = ledger.recordIfNew(event, payload);
    if (!fresh) {
      log.info(
          "Idempotent replay of Stripe event id={} type={}", event.getId(), event.getType());
      return ResponseEntity.ok().build();
    }
    log.info("Processing Stripe webhook event type={} id={}", event.getType(), event.getId());
    // Dispatch to domain handlers once implemented (customer.subscription.* etc.).
    return ResponseEntity.ok().build();
  }
}
