package com.pdfmaster.notification.application;

import com.pdfmaster.notification.domain.NotificationTemplate;
import java.util.Optional;

/** Outbound port for template persistence. */
public interface NotificationTemplateRepository {

  NotificationTemplate save(NotificationTemplate template);

  Optional<NotificationTemplate> findByCodeAndLocale(String code, String locale);
}
