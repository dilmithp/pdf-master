package com.pdfmaster.notification.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Notification-service has no user-scoped operational data today; only audit_log rows are removed.
 */
@Component
public class AccountDeletedListener {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletedListener.class);

  private final SpringDataAuditLogRepository auditLogRepository;

  public AccountDeletedListener(SpringDataAuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  @RabbitListener(queues = "account.deleted.notification")
  @Transactional
  public void onAccountDeleted(AccountDeletedEvent event) {
    log.info("Cascade deletion for notification userId={}", event.userId());
    auditLogRepository.deleteByUserId(event.userId());
    log.info("Deleted audit_log rows for userId={}", event.userId());
  }
}
