package com.pdfmaster.billing;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.billing.application.SubscriptionService;
import com.pdfmaster.billing.domain.Plan;
import com.pdfmaster.billing.domain.Subscription;
import com.pdfmaster.billing.domain.SubscriptionStatus;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Exercises {@code GET /v1/subscriptions/{userId}} round-trip through SubscriptionService. */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SubscriptionControllerIntegrationTest {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired MockMvc mockMvc;
  @Autowired SubscriptionService service;

  @Test
  void returnsSubscriptionWhenPresent() throws Exception {
    UUID userId = UUID.randomUUID();
    String stripeId = "sub_" + UUID.randomUUID();
    service.upsert(
        new Subscription(
            UUID.randomUUID(),
            userId,
            stripeId,
            Plan.PRO,
            SubscriptionStatus.ACTIVE,
            Instant.now().plusSeconds(86_400)));

    mockMvc
        .perform(
            get("/v1/subscriptions/" + userId)
                .with(jwt().jwt(j -> j.claim("sub", userId.toString()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.plan").value("PRO"))
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.stripeSubscriptionId").value(stripeId));
  }

  @Test
  void returns404WhenAbsent() throws Exception {
    UUID userId = UUID.randomUUID();
    mockMvc
        .perform(
            get("/v1/subscriptions/" + userId)
                .with(jwt().jwt(j -> j.claim("sub", userId.toString()))))
        .andExpect(status().isNotFound());
  }

  @Test
  void unauthenticatedRequestReturns401() throws Exception {
    mockMvc
        .perform(get("/v1/subscriptions/" + UUID.randomUUID()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void wrongOwnerReturns403() throws Exception {
    UUID userId = UUID.randomUUID();
    service.upsert(
        new Subscription(
            UUID.randomUUID(),
            userId,
            "sub_" + UUID.randomUUID(),
            Plan.FREE,
            SubscriptionStatus.ACTIVE,
            Instant.now().plusSeconds(86_400)));

    mockMvc
        .perform(
            get("/v1/subscriptions/" + userId)
                .with(jwt().jwt(j -> j.claim("sub", UUID.randomUUID().toString()))))
        .andExpect(status().isForbidden());
  }

  @Test
  void actuatorHealthIsPublic() throws Exception {
    mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }
}
