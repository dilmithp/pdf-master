package com.pdfmaster.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.billing.adapter.out.persistence.SubscriptionRepositoryAdapter;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ApplicationTests {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired SubscriptionService service;
  @Autowired SubscriptionRepositoryAdapter repository;
  @Autowired MockMvc mockMvc;

  @Test
  void contextLoads() {
    assertThat(service).isNotNull();
  }

  @Test
  void roundTripsSubscription() {
    UUID userId = UUID.randomUUID();
    Subscription created =
        service.upsert(
            new Subscription(
                UUID.randomUUID(),
                userId,
                "sub_" + UUID.randomUUID(),
                Plan.PRO,
                SubscriptionStatus.ACTIVE,
                Instant.now().plusSeconds(86_400)));
    assertThat(repository.findByUserId(userId)).contains(created);
  }

  @Test
  void stripeWebhookRejectsBadSignature() throws Exception {
    mockMvc
        .perform(
            post("/v1/webhooks/stripe")
                .header("Stripe-Signature", "t=1,v1=invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"evt_test\",\"object\":\"event\"}"))
        .andExpect(status().isBadRequest());
  }
}
