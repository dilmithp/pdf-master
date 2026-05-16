package com.pdfmaster.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * End-to-end HTTP test for {@link com.pdfmaster.auth.adapter.in.UserController}: exercises register
 * → fetch → conflict-on-duplicate against a real Postgres via Testcontainers.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIntegrationTest {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper mapper;

  @Test
  void registersFetchesAndRejectsDuplicate() throws Exception {
    String email = "integration-" + System.nanoTime() + "@pdfmaster.test";
    String body = mapper.writeValueAsString(new Register(email, "correct-horse-battery-staple"));

    MvcResult created =
        mockMvc
            .perform(post("/v1/users").contentType("application/json").content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.status").value("PENDING_VERIFICATION"))
            .andReturn();

    JsonNode tree = mapper.readTree(created.getResponse().getContentAsString());
    String id = tree.get("id").asText();

    mockMvc
        .perform(get("/v1/users/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email));

    mockMvc
        .perform(post("/v1/users").contentType("application/json").content(body))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("email_already_registered"));
  }

  @Test
  void rejectsShortPassword() throws Exception {
    String body = mapper.writeValueAsString(new Register("short-pw@pdfmaster.test", "tooshort"));
    mockMvc
        .perform(post("/v1/users").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void rejectsInvalidEmail() throws Exception {
    String body = mapper.writeValueAsString(new Register("not-an-email", "correct-horse-battery"));
    mockMvc
        .perform(post("/v1/users").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void returns404ForUnknownUser() throws Exception {
    mockMvc.perform(get("/v1/users/00000000-0000-0000-0000-000000000000"))
        .andExpect(status().isNotFound());
  }

  record Register(String email, String password) {}
}
