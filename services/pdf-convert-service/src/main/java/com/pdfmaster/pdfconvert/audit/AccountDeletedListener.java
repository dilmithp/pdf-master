package com.pdfmaster.pdfconvert.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountDeletedListener {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletedListener.class);

  private final SpringDataAuditLogRepository auditLogRepository;

  public AccountDeletedListener(SpringDataAuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  @RabbitListener(queues = "account.deleted.pdf-convert")
  @Transactional
  public void onAccountDeleted(AccountDeletedEvent event) {
    log.info("Cascade deletion for pdf-convert userId={}", event.userId());
    auditLogRepository.deleteByUserId(event.userId());
    log.info("Deleted audit_log rows for userId={}", event.userId());
  }
}
