package com.pdfmaster.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pdfmaster.billing.adapter.out.persistence.SpringDataStripeEventRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Verifies that duplicate Stripe webhook deliveries are handled idempotently.
 *
 * <p>HMAC signing approach: Stripe signs webhooks using {@code HMAC-SHA256(secret, "t=<ts>,v1=<prev_v1>")}.
 * Because {@code Webhook.computeHmacSha256} is package-private in the Stripe SDK, we replicate the
 * algorithm here directly using {@code javax.crypto.Mac} — this is exactly the same computation
 * Stripe performs on the consumer side in {@code Webhook.constructEvent}. This avoids a dependency
 * on any private Stripe API while still exercising the full controller + signature verification
 * + ledger path.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(
    properties = {
      "stripe.api-key=sk_test_placeholder",
      "stripe.webhook-secret=whsec_testsecret1234567890abcdef"
    })
class StripeIdempotencyIntegrationTest {

  private static final String WEBHOOK_SECRET = "whsec_testsecret1234567890abcdef";

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired MockMvc mockMvc;
  @Autowired SpringDataStripeEventRepository stripeEventRepository;

  @BeforeEach
  void cleanLedger() {
    stripeEventRepository.deleteAll();
  }

  @Test
  void firstDeliveryIsAcceptedAndRecorded() throws Exception {
    String eventId = "evt_test_" + System.nanoTime();
    String payload = buildPayload(eventId, "customer.subscription.updated");
    String signature = buildSignature(payload, WEBHOOK_SECRET);

    mockMvc
        .perform(
            post("/v1/webhooks/stripe")
                .header("Stripe-Signature", signature)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(status().isOk());

    assertThat(stripeEventRepository.existsById(eventId)).isTrue();
    assertThat(stripeEventRepository.count()).isEqualTo(1L);
  }

  @Test
  void duplicateDeliveryReturns200AndDoesNotInsertSecondRow() throws Exception {
    String eventId = "evt_test_" + System.nanoTime();
    String payload = buildPayload(eventId, "customer.subscription.created");
    String signature = buildSignature(payload, WEBHOOK_SECRET);

    // First delivery
    mockMvc
        .perform(
            post("/v1/webhooks/stripe")
                .header("Stripe-Signature", signature)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(status().isOk());

    // Stripe retry — same event id
    mockMvc
        .perform(
            post("/v1/webhooks/stripe")
                .header("Stripe-Signature", signature)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(status().isOk());

    // Only one row must exist regardless of how many times the event was delivered.
    assertThat(stripeEventRepository.count()).isEqualTo(1L);
  }

  @Test
  void badSignatureIsRejectedWith400() throws Exception {
    String payload = buildPayload("evt_bad_sig", "customer.subscription.updated");

    mockMvc
        .perform(
            post("/v1/webhooks/stripe")
                .header("Stripe-Signature", "t=1,v1=invalidsignaturehex")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        .andExpect(status().isBadRequest());

    assertThat(stripeEventRepository.count()).isZero();
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  /**
   * Builds a minimal Stripe event JSON string matching the shape Stripe actually sends.
   * The Stripe SDK's {@code constructEvent} checks {@code "object": "event"} in the top-level.
   */
  private static String buildPayload(String eventId, String eventType) {
    return String.format(
        "{\"id\":\"%s\",\"object\":\"event\",\"type\":\"%s\",\"livemode\":false,"
            + "\"created\":%d,\"data\":{\"object\":{}},\"pending_webhooks\":1,"
            + "\"request\":{\"id\":null,\"idempotency_key\":null}}",
        eventId, eventType, Instant.now().getEpochSecond());
  }

  /**
   * Replicates Stripe's signing scheme: {@code t=<timestamp>,v1=<HMAC-SHA256(secret, "t.<payload>")>}.
   * The signed string Stripe constructs is {@code "<timestamp>.<payload>"}.
   */
  private static String buildSignature(String payload, String secret) throws Exception {
    long timestamp = Instant.now().getEpochSecond();
    String signedPayload = timestamp + "." + payload;

    Mac mac = Mac.getInstance("HmacSHA256");
    // Stripe's secret is the raw string value after the "whsec_" prefix is stripped.
    String rawSecret = secret.startsWith("whsec_") ? secret.substring(6) : secret;
    mac.init(new SecretKeySpec(rawSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    byte[] hmacBytes = mac.doFinal(signedPayload.getBytes(StandardCharsets.UTF_8));

    StringBuilder hex = new StringBuilder();
    for (byte b : hmacBytes) {
      hex.append(String.format("%02x", b));
    }

    return "t=" + timestamp + ",v1=" + hex;
  }
}
