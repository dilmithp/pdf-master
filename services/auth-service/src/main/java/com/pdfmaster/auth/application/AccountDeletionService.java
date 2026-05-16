package com.pdfmaster.auth.application;

import com.pdfmaster.auth.adapter.out.persistence.UserEntity;
import com.pdfmaster.auth.adapter.out.persistence.UserJpaRepository;
import com.pdfmaster.auth.adapter.out.persistence.UserStatusEntity;
import com.pdfmaster.auth.audit.AccountDeletedEvent;
import com.pdfmaster.auth.audit.AuditConfig;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles the GDPR right-to-erasure request. Marks the local user as DEACTIVATED and publishes
 * an {@link AccountDeletedEvent} so every service can cascade-delete its user-owned data.
 */
@Service
public class AccountDeletionService {

  private static final Logger log = LoggerFactory.getLogger(AccountDeletionService.class);

  private final UserJpaRepository userJpaRepository;
  private final RabbitTemplate rabbitTemplate;
  private final Clock clock;

  public AccountDeletionService(
      UserJpaRepository userJpaRepository, RabbitTemplate rabbitTemplate, Clock clock) {
    this.userJpaRepository = userJpaRepository;
    this.rabbitTemplate = rabbitTemplate;
    this.clock = clock;
  }

  @Transactional
  public void deleteUser(String userId) {
    UUID id = UUID.fromString(userId);
    userJpaRepository.findById(id).ifPresent(user -> {
      UserEntity deactivated =
          new UserEntity(
              user.getId(),
              user.getEmail(),
              user.getPasswordHash(),
              UserStatusEntity.DEACTIVATED,
              user.getCreatedAt(),
              Instant.now(clock));
      userJpaRepository.save(deactivated);
      log.info("Marked user {} as DEACTIVATED", userId);
    });

    AccountDeletedEvent event = new AccountDeletedEvent(userId, Instant.now(clock));
    rabbitTemplate.convertAndSend(
        AuditConfig.ACCOUNT_EVENTS_EXCHANGE,
        AuditConfig.ACCOUNT_DELETED_ROUTING_KEY,
        event);
    log.info("Published AccountDeletedEvent for userId={}", userId);
  }
}
