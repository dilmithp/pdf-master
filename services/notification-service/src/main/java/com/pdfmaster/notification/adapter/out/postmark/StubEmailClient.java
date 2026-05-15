package com.pdfmaster.notification.adapter.out.postmark;

import com.pdfmaster.notification.application.EmailClient;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** In-memory {@link EmailClient} used in tests and local profiles to avoid Postmark calls. */
@Component
@Profile("test")
public class StubEmailClient implements EmailClient {

  @Override
  public String send(String recipient, String subject, String body) {
    return "stub-" + UUID.randomUUID();
  }
}
