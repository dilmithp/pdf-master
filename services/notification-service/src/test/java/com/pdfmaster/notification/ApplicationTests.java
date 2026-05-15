package com.pdfmaster.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdfmaster.notification.adapter.out.persistence.NotificationTemplateRepositoryAdapter;
import com.pdfmaster.notification.application.NotificationService;
import com.pdfmaster.notification.domain.NotificationTemplate;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ApplicationTests {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management-alpine"));

  @Autowired NotificationService service;
  @Autowired NotificationTemplateRepositoryAdapter repository;

  @Test
  void contextLoads() {
    assertThat(service).isNotNull();
  }

  @Test
  void roundTripsTemplateAndSendsViaStub() {
    NotificationTemplate template =
        repository.save(
            new NotificationTemplate(
                UUID.randomUUID(),
                "user.welcome",
                "en",
                "Welcome, {{name}}!",
                "<p>Hi {{name}}, thanks for signing up.</p>"));
    assertThat(repository.findByCodeAndLocale(template.code(), template.locale())).isPresent();

    String messageId =
        service.send("user.welcome", "en", "alice@example.com", Map.of("name", "Alice"));
    assertThat(messageId).startsWith("stub-");
  }
}
