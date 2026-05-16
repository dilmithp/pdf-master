package com.pdfmaster.pdfconvert.audit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {

  void deleteByUserId(String userId);
}
