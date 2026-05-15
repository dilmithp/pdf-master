package com.pdfmaster.billing.adapter.out.persistence;

import com.pdfmaster.billing.application.SubscriptionRepository;
import com.pdfmaster.billing.domain.Subscription;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Adapts the JPA repository to the application-layer port. */
@Repository
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

  private final SubscriptionJpaRepository jpa;

  public SubscriptionRepositoryAdapter(SubscriptionJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Subscription save(Subscription s) {
    SubscriptionEntity saved =
        jpa.save(
            new SubscriptionEntity(
                s.id(),
                s.userId(),
                s.stripeSubscriptionId(),
                s.plan(),
                s.status(),
                s.currentPeriodEnd()));
    return toDomain(saved);
  }

  @Override
  public Optional<Subscription> findByUserId(UUID userId) {
    return jpa.findByUserId(userId).map(SubscriptionRepositoryAdapter::toDomain);
  }

  private static Subscription toDomain(SubscriptionEntity e) {
    return new Subscription(
        e.getId(),
        e.getUserId(),
        e.getStripeSubscriptionId(),
        e.getPlan(),
        e.getStatus(),
        e.getCurrentPeriodEnd());
  }
}
