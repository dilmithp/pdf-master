package com.pdfmaster.esign;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * End-to-end HTTP test for SignatureRequestController. Exercises JSONB signer persistence and
 * round-trip through a real Postgres instance.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SignatureRequestControllerIntegrationTest {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper mapper;

  @Test
  void createsAndFetchesWithJsonbSigners() throws Exception {
    UUID senderId = UUID.randomUUID();
    String body =
        mapper.writeValueAsString(
            Map.of(
                "senderId", senderId,
                "documentS3Key", "envelope-" + senderId + "/document.pdf",
                "signers",
                    List.of(
                        Map.of("email", "alice@example.com", "order", 0),
                        Map.of("email", "bob@example.com", "order", 1))));

    MvcResult created =
        mockMvc
            .perform(
                post("/v1/signature-requests")
                    .contentType("application/json")
                    .content(body)
                    .with(jwt()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("DRAFT"))
            .andExpect(jsonPath("$.signers", org.hamcrest.Matchers.hasSize(2)))
            .andExpect(jsonPath("$.signers[0].email").value("alice@example.com"))
            .andExpect(jsonPath("$.signers[1].order").value(1))
            .andReturn();

    String id = mapper.readTree(created.getResponse().getContentAsString()).get("id").asText();

    mockMvc
        .perform(get("/v1/signature-requests/" + id).with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.signers[1].email").value("bob@example.com"));
  }

  @Test
  void rejectsEmptySigners() throws Exception {
    String body =
        mapper.writeValueAsString(
            Map.of(
                "senderId", UUID.randomUUID(),
                "documentS3Key", "envelope/document.pdf",
                "signers", List.of()));

    mockMvc
        .perform(
            post("/v1/signature-requests")
                .contentType("application/json")
                .content(body)
                .with(jwt()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void rejectsBadEmail() throws Exception {
    String body =
        mapper.writeValueAsString(
            Map.of(
                "senderId", UUID.randomUUID(),
                "documentS3Key", "envelope/document.pdf",
                "signers", List.of(Map.of("email", "not-an-email", "order", 0))));

    mockMvc
        .perform(
            post("/v1/signature-requests")
                .contentType("application/json")
                .content(body)
                .with(jwt()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void unauthenticatedPostReturns401() throws Exception {
    String body =
        mapper.writeValueAsString(
            Map.of(
                "senderId", UUID.randomUUID(),
                "documentS3Key", "envelope/document.pdf",
                "signers", List.of(Map.of("email", "alice@example.com", "order", 0))));

    mockMvc
        .perform(post("/v1/signature-requests").contentType("application/json").content(body))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void unauthenticatedGetReturns401() throws Exception {
    mockMvc
        .perform(get("/v1/signature-requests/00000000-0000-0000-0000-000000000000"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void actuatorHealthIsPublic() throws Exception {
    mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }
}
