package com.pdfmaster.billing.adapter.in;

import com.pdfmaster.billing.application.SubscriptionService;
import com.pdfmaster.billing.domain.Subscription;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST adapter exposing read access to a user's current subscription. */
@RestController
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {

  private final SubscriptionService service;

  public SubscriptionController(SubscriptionService service) {
    this.service = service;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<SubscriptionResponse> get(@PathVariable UUID userId) {
    return service
        .findByUserId(userId)
        .map(SubscriptionResponse::from)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  public record SubscriptionResponse(
      UUID id,
      UUID userId,
      String stripeSubscriptionId,
      String plan,
      String status,
      Instant currentPeriodEnd) {

    static SubscriptionResponse from(Subscription s) {
      return new SubscriptionResponse(
          s.id(),
          s.userId(),
          s.stripeSubscriptionId(),
          s.plan().name(),
          s.status().name(),
          s.currentPeriodEnd());
    }
  }
}
