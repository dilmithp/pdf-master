package com.pdfmaster.billing.application;

import com.pdfmaster.billing.domain.Subscription;
import java.util.Optional;
import java.util.UUID;

/** Outbound port for subscription persistence. */
public interface SubscriptionRepository {

  Subscription save(Subscription subscription);

  Optional<Subscription> findByUserId(UUID userId);
}
