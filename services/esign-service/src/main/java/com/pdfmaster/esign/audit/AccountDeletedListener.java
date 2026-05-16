package com.pdfmaster.esign.audit;

import com.pdfmaster.esign.adapter.out.persistence.SignatureRequestJpaRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Removes signature requests where sender_id matches the deleted user.
 */
@Component
public class AccountDeletedListener {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletedListener.class);

  private final SignatureRequestJpaRepository signatureRequestJpaRepository;
  private final SpringDataAuditLogRepository auditLogRepository;

  public AccountDeletedListener(
      SignatureRequestJpaRepository signatureRequestJpaRepository,
      SpringDataAuditLogRepository auditLogRepository) {
    this.signatureRequestJpaRepository = signatureRequestJpaRepository;
    this.auditLogRepository = auditLogRepository;
  }

  @RabbitListener(queues = "account.deleted.esign")
  @Transactional
  public void onAccountDeleted(AccountDeletedEvent event) {
    log.info("Cascade deletion for esign userId={}", event.userId());

    try {
      UUID uid = UUID.fromString(event.userId());
      long count = signatureRequestJpaRepository.deleteBySenderId(uid);
      log.info("Deleted {} signature_request rows for userId={}", count, event.userId());
    } catch (Exception ex) {
      log.warn("Error deleting signature requests for userId={}: {}", event.userId(), ex.getMessage());
    }

    auditLogRepository.deleteByUserId(event.userId());
    log.info("Deleted audit_log rows for userId={}", event.userId());
  }
}
