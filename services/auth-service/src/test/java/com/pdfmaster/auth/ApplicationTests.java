package com.pdfmaster.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdfmaster.auth.adapter.out.persistence.UserRepositoryAdapter;
import com.pdfmaster.auth.application.UserRegistrationService;
import com.pdfmaster.auth.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ApplicationTests {

  @Container @ServiceConnection
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired UserRegistrationService registrationService;
  @Autowired UserRepositoryAdapter repositoryAdapter;

  @Test
  void contextLoads() {
    assertThat(registrationService).isNotNull();
  }

  @Test
  void registersAndFindsUser() {
    User created = registrationService.register("scaffold@pdfmaster.test", "correct-horse-battery");
    assertThat(created.id()).isNotNull();
    assertThat(repositoryAdapter.findById(created.id())).isPresent();
    assertThat(repositoryAdapter.findByEmail("scaffold@pdfmaster.test")).isPresent();
  }
}
