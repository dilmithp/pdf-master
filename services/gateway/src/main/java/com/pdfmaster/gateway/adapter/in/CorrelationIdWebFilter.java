package com.pdfmaster.gateway.adapter.in;

import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Ensures every request flowing through the gateway is tagged with an {@code X-Correlation-Id}.
 *
 * <p>If the inbound request already carries a correlation id we honour it (assuming an upstream
 * caller — Cloudflare or a mobile client — is propagating one). Otherwise we mint a fresh UUID. The
 * id is mirrored onto the response so clients can log it on their side, stored on the Reactor
 * context for downstream propagation, and bound to MDC so it appears in structured logs for the
 * duration of the request.
 */
@Component
public class CorrelationIdWebFilter implements WebFilter, Ordered {

  public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
  public static final String CORRELATION_ID_CONTEXT_KEY = "correlationId";
  public static final String MDC_KEY = "correlationId";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String existing = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);
    String correlationId =
        (existing == null || existing.isBlank()) ? UUID.randomUUID().toString() : existing;

    ServerHttpRequest mutatedRequest =
        exchange
            .getRequest()
            .mutate()
            .headers(headers -> headers.set(CORRELATION_ID_HEADER, correlationId))
            .build();

    exchange.getResponse().getHeaders().set(CORRELATION_ID_HEADER, correlationId);

    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

    MDC.put(MDC_KEY, correlationId);
    return chain
        .filter(mutatedExchange)
        .contextWrite(ctx -> ctx.put(CORRELATION_ID_CONTEXT_KEY, correlationId))
        .doFinally(signal -> MDC.remove(MDC_KEY));
  }

  @Override
  public int getOrder() {
    // Run before Spring Cloud Gateway's routing filters so the id is available everywhere.
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
