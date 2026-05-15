package com.pdfmaster.notification.domain;

import java.util.Objects;
import java.util.UUID;

/** Email template aggregate. Pure domain — no Spring/JPA dependencies. */
public record NotificationTemplate(
    UUID id, String code, String locale, String subjectTemplate, String bodyTemplate) {

  public NotificationTemplate {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(code, "code");
    Objects.requireNonNull(locale, "locale");
    Objects.requireNonNull(subjectTemplate, "subjectTemplate");
    Objects.requireNonNull(bodyTemplate, "bodyTemplate");
  }
}
