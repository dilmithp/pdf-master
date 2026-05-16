package com.pdfmaster.pdfcore.audit;

import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditPersistenceService {

  private static final Logger log = LoggerFactory.getLogger(AuditPersistenceService.class);
  private final SpringDataAuditLogRepository repository;

  public AuditPersistenceService(SpringDataAuditLogRepository repository) {
    this.repository = repository;
  }

  @Async
  @Transactional
  public void persist(String userId, String action, String resource,
      String ip, String userAgent, Short statusCode, String error) {
    try {
      repository.save(new AuditLogEntity(UUID.randomUUID(), userId, action, resource,
          ip, userAgent, statusCode, error, null, Instant.now()));
    } catch (Exception ex) {
      log.warn("Failed to persist audit log entry: {}", ex.getMessage());
    }
  }
}
