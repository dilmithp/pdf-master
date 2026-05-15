package com.pdfmaster.billing.application;

import com.pdfmaster.billing.domain.Subscription;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Read/write use cases for billing subscriptions. */
@Service
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public SubscriptionService(SubscriptionRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public Optional<Subscription> findByUserId(UUID userId) {
    return repository.findByUserId(userId);
  }

  @Transactional
  public Subscription upsert(Subscription subscription) {
    return repository.save(subscription);
  }
}
