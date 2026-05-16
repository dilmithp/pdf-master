package com.pdfmaster.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.billing.audit.SpringDataAuditLogRepository;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Verifies AuditFilter persists an audit_log row after each HTTP request.
 */
@SpringBootTest(properties = {"spring.autoconfigure.exclude="})
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
    String userId = UUID.randomUUID().toString();

    mockMvc
        .perform(
            get("/v1/subscriptions/" + userId)
                .with(jwt().jwt(j -> j.claim("sub", userId))))
        .andExpect(status().isNotFound());

    await()
        .atMost(Duration.ofSeconds(1))
        .untilAsserted(() -> assertThat(auditRepo.count()).isGreaterThan(before));
  }
}
