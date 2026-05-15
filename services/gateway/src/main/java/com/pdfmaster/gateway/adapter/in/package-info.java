/**
 * Inbound adapters for the gateway service.
 *
 * <p>Driving side of the hexagon: HTTP controllers and {@link
 * org.springframework.web.server.WebFilter} implementations that translate transport-level concerns
 * (headers, status codes, request lifecycle) into calls against application services. Classes here
 * are named with the {@code Controller}, {@code Filter}, or {@code WebFilter} suffix.
 */
package com.pdfmaster.gateway.adapter.in;
