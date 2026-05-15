package com.pdfmaster.gateway;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

/**
 * Smoke test that verifies the gateway boots end-to-end and exposes the actuator health endpoint
 * without authentication. External dependencies (Redis, OAuth2 issuer) are stubbed with primary
 * test beans so the test is hermetic.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:0/test-issuer",
      "management.endpoint.health.probes.enabled=true",
      "management.health.redis.enabled=false"
    })
class ApplicationTests {

  @Autowired private WebTestClient webTestClient;

  @Test
  void healthEndpointIsAvailableWithoutAuth() {
    webTestClient
        .get()
        .uri("/actuator/health")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("UP");
  }

  @TestConfiguration
  static class TestBeans {

    @Bean
    @Primary
    ReactiveJwtDecoder testJwtDecoder() {
      return mock(ReactiveJwtDecoder.class);
    }

    @Bean
    @Primary
    ReactiveRedisConnectionFactory testRedisConnectionFactory() {
      ReactiveRedisConnectionFactory factory = mock(ReactiveRedisConnectionFactory.class);
      ReactiveRedisConnection connection = mock(ReactiveRedisConnection.class);
      when(factory.getReactiveConnection()).thenReturn(connection);
      when(connection.closeLater()).thenReturn(Mono.empty());
      return factory;
    }
  }
}
