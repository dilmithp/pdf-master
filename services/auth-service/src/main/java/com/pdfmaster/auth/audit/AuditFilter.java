package com.pdfmaster.auth.audit;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import org.springframework.stereotype.Component;

/**
 * Servlet filter that captures every HTTP request and persists an {@link AuditLogEntity}
 * asynchronously via {@link AuditPersistenceService}. Skips actuator and favicon noise.
 * Never throws — failures are logged as warnings to avoid blocking the request.
 */
@Component
public class AuditFilter implements Filter {

  private final AuditPersistenceService persistence;

  public AuditFilter(AuditPersistenceService persistence) {
    this.persistence = persistence;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest  req  = (HttpServletRequest)  request;
    HttpServletResponse resp = (HttpServletResponse) response;

    String path = req.getRequestURI();
    if (path.startsWith("/actuator") || path.equals("/favicon.ico")) {
      chain.doFilter(request, response);
      return;
    }

    try {
      chain.doFilter(request, response);
    } finally {
      try {
        String userId = resolveUserId(req);
        String action = req.getMethod() + " " + path;
        String ip = resolveIp(req);
        String ua = truncate(req.getHeader("User-Agent"), 512);
        short  sc = (short) resp.getStatus();
        String error = sc >= 400 ? "HTTP " + sc : null;
        persistence.persist(userId, action, path, ip, ua, sc, error);
      } catch (Exception ex) {
        // never propagate
      }
    }
  }

  private static String resolveUserId(HttpServletRequest req) {
    Principal p = req.getUserPrincipal();
    return p != null ? p.getName() : null;
  }

  private static String resolveIp(HttpServletRequest req) {
    String forwarded = req.getHeader("X-Forwarded-For");
    if (forwarded != null && !forwarded.isBlank()) {
      int comma = forwarded.indexOf(',');
      return comma >= 0 ? forwarded.substring(0, comma).trim() : forwarded.trim();
    }
    return req.getRemoteAddr();
  }

  private static String truncate(String value, int max) {
    if (value == null) return null;
    return value.length() > max ? value.substring(0, max) : value;
  }
}
