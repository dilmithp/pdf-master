package com.pdfmaster.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.auth.audit.SpringDataAuditLogRepository;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Verifies that every HTTP request results in an audit_log row being persisted
 * within 1 second (async flush via AuditPersistenceService).
 */
@SpringBootTest(
    properties = {
      "management.health.redis.enabled=false",
      "spring.data.redis.repositories.enabled=false",
      "spring.autoconfigure.exclude="
    })
@AutoConfigureMockMvc
@Testcontainers
class AuditFilterIntegrationTest {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Container @ServiceConnection
  static final RabbitMQContainer RABBIT =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management-alpine"));

  @Autowired MockMvc mockMvc;
  @Autowired SpringDataAuditLogRepository auditRepo;

  @Test
  void auditRowIsPersistedAfterRequest() throws Exception {
    long before = auditRepo.count();

    mockMvc
        .perform(post("/v1/users").contentType("application/json")
            .content("{\"email\":\"audit-test@pdfmaster.test\",\"password\":\"correct-horse-battery\"}"))
        .andExpect(status().isCreated());

    await()
        .atMost(Duration.ofSeconds(1))
        .untilAsserted(() -> assertThat(auditRepo.count()).isGreaterThan(before));
  }
}
