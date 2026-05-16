package com.pdfmaster.esign.audit;

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
        Principal p = req.getUserPrincipal();
        String userId = p != null ? p.getName() : null;
        String action = req.getMethod() + " " + path;
        String forwarded = req.getHeader("X-Forwarded-For");
        String ip = (forwarded != null && !forwarded.isBlank())
            ? forwarded.split(",")[0].trim() : req.getRemoteAddr();
        String ua = req.getHeader("User-Agent");
        if (ua != null && ua.length() > 512) ua = ua.substring(0, 512);
        short sc = (short) resp.getStatus();
        persistence.persist(userId, action, path, ip, ua, sc, sc >= 400 ? "HTTP " + sc : null);
      } catch (Exception ex) { /* never propagate */ }
    }
  }
}
