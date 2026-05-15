package com.pdfmaster.notification.adapter.out.persistence;

import com.pdfmaster.notification.application.NotificationTemplateRepository;
import com.pdfmaster.notification.domain.NotificationTemplate;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** Adapts the JPA repository to the application-layer port. */
@Repository
public class NotificationTemplateRepositoryAdapter implements NotificationTemplateRepository {

  private final NotificationTemplateJpaRepository jpa;

  public NotificationTemplateRepositoryAdapter(NotificationTemplateJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public NotificationTemplate save(NotificationTemplate template) {
    NotificationTemplateEntity saved =
        jpa.save(
            new NotificationTemplateEntity(
                template.id(),
                template.code(),
                template.locale(),
                template.subjectTemplate(),
                template.bodyTemplate()));
    return toDomain(saved);
  }

  @Override
  public Optional<NotificationTemplate> findByCodeAndLocale(String code, String locale) {
    return jpa.findByCodeAndLocale(code, locale)
        .map(NotificationTemplateRepositoryAdapter::toDomain);
  }

  private static NotificationTemplate toDomain(NotificationTemplateEntity e) {
    return new NotificationTemplate(
        e.getId(), e.getCode(), e.getLocale(), e.getSubjectTemplate(), e.getBodyTemplate());
  }
}
