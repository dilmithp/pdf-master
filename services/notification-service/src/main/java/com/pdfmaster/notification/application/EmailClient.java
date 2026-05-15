package com.pdfmaster.notification.application;

/** Outbound port for sending a rendered email. Implemented by an external provider adapter. */
public interface EmailClient {

  /**
   * @return provider-assigned message id (e.g., Postmark MessageID).
   */
  String send(String recipient, String subject, String body);
}
