package com.pdfmaster.auth.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pdfmaster.auth.adapter.out.persistence.UserJpaRepository;

/**
 * Listens for account-deletion cascade events and removes auth-service user data.
 * Deletes the users row and all audit log rows for that user.
 */
@Component
public class AccountDeletedListener {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletedListener.class);

  private final UserJpaRepository userJpaRepository;
  private final SpringDataAuditLogRepository auditLogRepository;

  public AccountDeletedListener(
      UserJpaRepository userJpaRepository,
      SpringDataAuditLogRepository auditLogRepository) {
    this.userJpaRepository = userJpaRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @RabbitListener(queues = "account.deleted.auth")
  @Transactional
  public void onAccountDeleted(AccountDeletedEvent event) {
    log.info("Processing account deletion cascade for userId={}", event.userId());

    try {
      userJpaRepository.deleteById(java.util.UUID.fromString(event.userId()));
      log.info("Deleted user record for userId={}", event.userId());
    } catch (Exception ex) {
      log.warn("Could not delete user row for userId={}: {}", event.userId(), ex.getMessage());
    }

    auditLogRepository.deleteByUserId(event.userId());
    log.info("Deleted audit_log rows for userId={}", event.userId());
  }
}
