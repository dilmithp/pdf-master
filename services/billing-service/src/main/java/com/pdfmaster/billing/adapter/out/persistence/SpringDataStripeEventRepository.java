package com.pdfmaster.billing.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link StripeEventEntity}. Key is the Stripe event id string. */
public interface SpringDataStripeEventRepository
    extends JpaRepository<StripeEventEntity, String> {}
