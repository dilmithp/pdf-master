package com.pdfmaster.auth.audit;

import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Asynchronous writer so the audit flush never delays the HTTP response. */
@Service
public class AuditPersistenceService {

  private static final Logger log = LoggerFactory.getLogger(AuditPersistenceService.class);

  private final SpringDataAuditLogRepository repository;

  public AuditPersistenceService(SpringDataAuditLogRepository repository) {
    this.repository = repository;
  }

  @Async
  @Transactional
  public void persist(
      String userId,
      String action,
      String resource,
      String ip,
      String userAgent,
      Short statusCode,
      String error) {
    try {
      AuditLogEntity entry =
          new AuditLogEntity(
              UUID.randomUUID(),
              userId,
              action,
              resource,
              ip,
              userAgent,
              statusCode,
              error,
              null,
              Instant.now());
      repository.save(entry);
    } catch (Exception ex) {
      log.warn("Failed to persist audit log entry: {}", ex.getMessage());
    }
  }
}
