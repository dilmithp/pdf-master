package com.pdfmaster.notification.application;

import com.pdfmaster.notification.domain.NotificationTemplate;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Renders a template and dispatches it via the configured {@link EmailClient}. */
@Service
public class NotificationService {

  private final NotificationTemplateRepository templates;
  private final EmailClient emailClient;

  public NotificationService(NotificationTemplateRepository templates, EmailClient emailClient) {
    this.templates = templates;
    this.emailClient = emailClient;
  }

  @Transactional(readOnly = true)
  public String send(
      String templateCode, String locale, String recipient, Map<String, String> vars) {
    NotificationTemplate template =
        templates
            .findByCodeAndLocale(templateCode, locale)
            .orElseThrow(() -> new TemplateNotFoundException(templateCode, locale));
    Map<String, String> safeVars = vars == null ? Map.of() : vars;
    String subject = render(template.subjectTemplate(), safeVars);
    String body = render(template.bodyTemplate(), safeVars);
    return emailClient.send(recipient, subject, body);
  }

  private static String render(String template, Map<String, String> vars) {
    String result = template;
    for (Map.Entry<String, String> entry : vars.entrySet()) {
      result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
    }
    return result;
  }
}
