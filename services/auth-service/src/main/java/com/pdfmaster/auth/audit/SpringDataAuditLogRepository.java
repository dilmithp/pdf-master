package com.pdfmaster.auth.audit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link AuditLogEntity}. */
public interface SpringDataAuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {

  void deleteByUserId(String userId);
}
