package com.pdfmaster.notification.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;

/** JPA record for {@code notification.notification_templates}. */
@Entity
@Table(
    name = "notification_templates",
    schema = "notification",
    uniqueConstraints =
        @UniqueConstraint(
            name = "templates_code_locale_uk",
            columnNames = {"code", "locale"}))
public class NotificationTemplateEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "code", nullable = false, length = 128)
  private String code;

  @Column(name = "locale", nullable = false, length = 16)
  private String locale;

  @Column(name = "subject_template", nullable = false, length = 512)
  private String subjectTemplate;

  @Column(name = "body_template", nullable = false, columnDefinition = "text")
  private String bodyTemplate;

  protected NotificationTemplateEntity() {
    // JPA
  }

  public NotificationTemplateEntity(
      UUID id, String code, String locale, String subjectTemplate, String bodyTemplate) {
    this.id = id;
    this.code = code;
    this.locale = locale;
    this.subjectTemplate = subjectTemplate;
    this.bodyTemplate = bodyTemplate;
  }

  public UUID getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getLocale() {
    return locale;
  }

  public String getSubjectTemplate() {
    return subjectTemplate;
  }

  public String getBodyTemplate() {
    return bodyTemplate;
  }
}
