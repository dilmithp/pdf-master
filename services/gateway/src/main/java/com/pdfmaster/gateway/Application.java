package com.pdfmaster.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import reactor.core.publisher.Hooks;

/**
 * Entry point for the PDF Master edge gateway.
 *
 * <p>This is the only reactive (Spring WebFlux / Spring Cloud Gateway) service in the platform. It
 * handles TLS termination at the edge, validates incoming JWTs against the auth service, applies
 * Redis-backed per-user rate limiting, and routes requests to the appropriate downstream service.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

  public static void main(String[] args) {
    // Propagate ThreadLocal-bound context (MDC, security context, correlation id) across reactor
    // boundaries so that logs and downstream calls retain request-scoped attributes.
    Hooks.enableAutomaticContextPropagation();
    SpringApplication.run(Application.class, args);
  }
}
