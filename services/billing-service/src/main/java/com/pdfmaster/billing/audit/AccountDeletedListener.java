package com.pdfmaster.billing.audit;

import com.pdfmaster.billing.adapter.out.persistence.SubscriptionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles the account-deletion cascade for billing.
 * Subscriptions are deleted. Stripe events are retained for legal audit purposes
 * (payment processor records must be kept 7 years in most jurisdictions).
 */
@Component
public class AccountDeletedListener {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletedListener.class);

  private final SubscriptionJpaRepository subscriptionJpaRepository;
  private final SpringDataAuditLogRepository auditLogRepository;

  public AccountDeletedListener(
      SubscriptionJpaRepository subscriptionJpaRepository,
      SpringDataAuditLogRepository auditLogRepository) {
    this.subscriptionJpaRepository = subscriptionJpaRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @RabbitListener(queues = "account.deleted.billing")
  @Transactional
  public void onAccountDeleted(AccountDeletedEvent event) {
    log.info("Cascade deletion for billing userId={}", event.userId());

    try {
      java.util.UUID uid = java.util.UUID.fromString(event.userId());
      long subCount = subscriptionJpaRepository.deleteByUserId(uid);
      log.info("Deleted {} subscription rows for userId={}", subCount, event.userId());
    } catch (Exception ex) {
      log.warn("Error deleting subscription for userId={}: {}", event.userId(), ex.getMessage());
    }

    auditLogRepository.deleteByUserId(event.userId());
    log.info("Deleted audit_log rows for userId={}", event.userId());
  }
}
