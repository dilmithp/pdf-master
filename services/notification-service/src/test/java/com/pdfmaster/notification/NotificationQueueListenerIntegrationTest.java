package com.pdfmaster.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.pdfmaster.notification.adapter.out.persistence.NotificationTemplateRepositoryAdapter;
import com.pdfmaster.notification.config.RabbitMqConfig;
import com.pdfmaster.notification.domain.NotificationEvent;
import com.pdfmaster.notification.domain.NotificationTemplate;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.TestConfiguration;
import com.pdfmaster.notification.application.EmailClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Exercises the full publish-to-consume loop: publish a NotificationEvent on the
 * {@code notifications} exchange and assert the {@code @RabbitListener} delivered it to a counted
 * email-client stub.
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class NotificationQueueListenerIntegrationTest {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management-alpine"));

  @Autowired RabbitTemplate rabbitTemplate;
  @Autowired NotificationTemplateRepositoryAdapter templates;
  @Autowired CountingEmailClient emailClient;

  @BeforeEach
  void resetCounter() {
    emailClient.reset();
  }

  @Test
  void publishedEventIsConsumedAndDelivered() {
    templates.save(
        new NotificationTemplate(
            UUID.randomUUID(),
            "user.welcome",
            "en",
            "Welcome, {{name}}!",
            "<p>Hi {{name}}.</p>"));

    NotificationEvent event =
        new NotificationEvent("user.welcome", "en", "alice@example.com", Map.of("name", "Alice"));
    rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, "notification.welcome", event);

    await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> assertThat(emailClient.count()).isEqualTo(1));
  }

  /** Replaces the real Postmark client during this test so we can observe delivery counts. */
  @TestConfiguration
  @Profile("test")
  static class StubEmailConfig {
    @Bean
    @Primary
    CountingEmailClient countingEmailClient() {
      return new CountingEmailClient();
    }
  }

  @Component
  static class CountingEmailClient implements EmailClient {
    private final AtomicInteger sent = new AtomicInteger();

    @Override
    public String send(String recipient, String subject, String htmlBody) {
      sent.incrementAndGet();
      return "stub-" + System.nanoTime();
    }

    int count() {
      return sent.get();
    }

    void reset() {
      sent.set(0);
    }
  }
}
