package com.pdfmaster.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Token-bucket parameters for the Redis-backed gateway rate limiter.
 *
 * @param replenishRate steady-state requests per second per key
 * @param burstCapacity maximum burst size for a single key
 * @param requestedTokens number of tokens consumed per request
 */
@ConfigurationProperties(prefix = "gateway.rate-limit")
public record RateLimitProperties(int replenishRate, int burstCapacity, int requestedTokens) {

  public RateLimitProperties {
    if (replenishRate <= 0) {
      replenishRate = 10;
    }
    if (burstCapacity <= 0) {
      burstCapacity = 20;
    }
    if (requestedTokens <= 0) {
      requestedTokens = 1;
    }
  }
}
