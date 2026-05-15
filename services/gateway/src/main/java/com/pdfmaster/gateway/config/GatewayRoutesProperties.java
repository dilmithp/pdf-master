package com.pdfmaster.gateway.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed configuration for downstream route URIs.
 *
 * <p>Each downstream service has an entry in {@link #routes()} keyed by the canonical service id
 * (e.g. {@code auth}, {@code pdf}, {@code ai}). Defaults point at the localhost dev ports; in
 * production these are overridden to {@code lb://<service>} via Kubernetes service DNS.
 */
@ConfigurationProperties(prefix = "gateway")
public record GatewayRoutesProperties(Map<String, String> routes) {

  public GatewayRoutesProperties {
    routes = routes == null ? Map.of() : Map.copyOf(routes);
  }

  /**
   * Returns the configured URI for the given route id, or the supplied default if no override was
   * provided.
   */
  public String uriFor(String id, String defaultUri) {
    return routes.getOrDefault(id, defaultUri);
  }
}
