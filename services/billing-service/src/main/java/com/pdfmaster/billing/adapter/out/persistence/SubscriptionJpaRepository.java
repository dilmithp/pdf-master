package com.pdfmaster.billing.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link SubscriptionEntity}. */
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, UUID> {

  Optional<SubscriptionEntity> findByUserId(UUID userId);
}
