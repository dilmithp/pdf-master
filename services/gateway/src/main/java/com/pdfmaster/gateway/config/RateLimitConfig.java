package com.pdfmaster.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Rate-limiting wiring.
 *
 * <p>The {@link KeyResolver} prefers the authenticated user's JWT {@code sub} claim so that limits
 * travel with the user across IPs. When no security context is present (e.g. an unauthenticated
 * request that has not yet been rejected), the remote address is used as a fallback so the bucket
 * is never silently shared across clients.
 */
@Configuration
public class RateLimitConfig {

  private static final String UNKNOWN_REMOTE = "unknown";

  @Bean
  public RedisRateLimiter redisRateLimiter(RateLimitProperties properties) {
    return new RedisRateLimiter(
        properties.replenishRate(), properties.burstCapacity(), properties.requestedTokens());
  }

  @Bean
  public KeyResolver userKeyResolver() {
    return exchange ->
        ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication())
            .filter(auth -> auth != null && auth.getPrincipal() instanceof Jwt)
            .map(auth -> ((Jwt) auth.getPrincipal()).getSubject())
            .switchIfEmpty(
                Mono.fromSupplier(
                    () -> {
                      var remote = exchange.getRequest().getRemoteAddress();
                      if (remote == null || remote.getAddress() == null) {
                        return UNKNOWN_REMOTE;
                      }
                      return remote.getAddress().getHostAddress();
                    }));
  }
}
