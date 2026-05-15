package com.pdfmaster.esign;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdfmaster.esign.adapter.out.persistence.SignatureRequestRepositoryAdapter;
import com.pdfmaster.esign.application.SignatureRequestService;
import com.pdfmaster.esign.domain.SignatureRequest;
import com.pdfmaster.esign.domain.Signer;
import java.util.List;
import java.util.UUID;
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

  @Autowired SignatureRequestService service;
  @Autowired SignatureRequestRepositoryAdapter repository;

  @Test
  void contextLoads() {
    assertThat(service).isNotNull();
  }

  @Test
  void roundTripsSignatureRequestWithJsonbSigners() {
    SignatureRequest created =
        service.create(
            UUID.randomUUID(),
            "envelope-x/document.pdf",
            List.of(
                new Signer("alice@example.com", 0, null), new Signer("bob@example.com", 1, null)));

    SignatureRequest reloaded = repository.findById(created.id()).orElseThrow();
    assertThat(reloaded.signers()).hasSize(2);
    assertThat(reloaded.signers().get(0).email()).isEqualTo("alice@example.com");
    assertThat(reloaded.signers().get(1).order()).isEqualTo(1);
  }
}
