package com.pdfmaster.notification.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link NotificationTemplateEntity}. */
public interface NotificationTemplateJpaRepository
    extends JpaRepository<NotificationTemplateEntity, UUID> {

  Optional<NotificationTemplateEntity> findByCodeAndLocale(String code, String locale);
}
