package com.pdfmaster.pdfai.audit;

import com.pdfmaster.pdfai.adapter.out.persistence.JpaJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * pdf-ai-service job records do not store user_id (job state is keyed by jobId, not userId).
 * Only audit_log rows are removed. Future migration should add user_id to pdf_ai.job.
 */
@Component
public class AccountDeletedListener {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletedListener.class);

  private final SpringDataAuditLogRepository auditLogRepository;

  public AccountDeletedListener(SpringDataAuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  @RabbitListener(queues = "account.deleted.pdf-ai")
  @Transactional
  public void onAccountDeleted(AccountDeletedEvent event) {
    log.info("Cascade deletion for pdf-ai userId={}", event.userId());
    auditLogRepository.deleteByUserId(event.userId());
    log.info("Deleted audit_log rows for userId={}", event.userId());
  }
}
