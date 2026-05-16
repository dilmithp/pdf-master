package com.pdfmaster.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfmaster.auth.adapter.out.persistence.UserJpaRepository;
import com.pdfmaster.auth.adapter.out.persistence.UserStatusEntity;
import com.pdfmaster.auth.audit.AccountDeletedEvent;
import com.pdfmaster.auth.audit.SpringDataAuditLogRepository;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Verifies DELETE /v1/account: marks user DEACTIVATED, publishes AccountDeletedEvent
 * (received by an in-test listener), and creates an audit_log entry.
 */
@SpringBootTest(
    properties = {
      "management.health.redis.enabled=false",
      "spring.data.redis.repositories.enabled=false",
      "spring.autoconfigure.exclude="
    })
@AutoConfigureMockMvc
@Testcontainers
class AccountDeletionIntegrationTest {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management-alpine"));

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper mapper;
  @Autowired UserJpaRepository userRepo;
  @Autowired SpringDataAuditLogRepository auditRepo;
  @Autowired EventCapture eventCapture;

  @Test
  void deleteAccountMarksUserDeactivatedAndPublishesEvent() throws Exception {
    String email = "delete-me-" + System.nanoTime() + "@pdfmaster.test";
    String body = mapper.writeValueAsString(new RegisterRequest(email, "correct-horse-battery-staple"));

    MvcResult created =
        mockMvc
            .perform(post("/v1/users").contentType("application/json").content(body))
            .andExpect(status().isCreated())
            .andReturn();

    JsonNode tree = mapper.readTree(created.getResponse().getContentAsString());
    String userId = tree.get("id").asText();

    long auditBefore = auditRepo.count();

    mockMvc
        .perform(delete("/v1/account").with(jwt().jwt(j -> j.claim("sub", userId))))
        .andExpect(status().isNoContent());

    // Verify user is marked DEACTIVATED
    UUID uid = UUID.fromString(userId);
    assertThat(userRepo.findById(uid))
        .isPresent()
        .hasValueSatisfying(u -> assertThat(u.getStatus()).isEqualTo(UserStatusEntity.DEACTIVATED));

    // Verify AccountDeletedEvent was published and received
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(() ->
            assertThat(eventCapture.getCaptured()).isNotNull()
                .satisfies(e -> assertThat(e.userId()).isEqualTo(userId)));

    // Verify audit_log row was written for the DELETE request
    await()
        .atMost(Duration.ofSeconds(2))
        .untilAsserted(() -> assertThat(auditRepo.count()).isGreaterThan(auditBefore));
  }

  /** Captures AccountDeletedEvent on a test-only queue bound to the same account.events exchange. */
  static class EventCapture {
    private final AtomicReference<AccountDeletedEvent> captured = new AtomicReference<>();

    @RabbitListener(queues = "account.deleted.test-capture")
    public void capture(AccountDeletedEvent event) {
      captured.set(event);
    }

    AccountDeletedEvent getCaptured() {
      return captured.get();
    }
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    EventCapture eventCapture() {
      return new EventCapture();
    }

    @Bean
    org.springframework.amqp.core.Queue testCaptureQueue() {
      return new org.springframework.amqp.core.Queue("account.deleted.test-capture", false, false, true);
    }

    @Bean
    org.springframework.amqp.core.Binding testCaptureBinding(
        org.springframework.amqp.core.Queue testCaptureQueue,
        org.springframework.amqp.core.TopicExchange accountEventsExchange) {
      return org.springframework.amqp.core.BindingBuilder
          .bind(testCaptureQueue).to(accountEventsExchange).with("account.deleted");
    }
  }

  record RegisterRequest(String email, String password) {}
}
