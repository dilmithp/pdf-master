package com.pdfmaster.auth.adapter.in;

import com.pdfmaster.auth.application.AccountDeletionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST adapter for account self-management (GDPR right to erasure). */
@RestController
@RequestMapping("/v1/account")
public class AccountController {

  private final AccountDeletionService service;

  public AccountController(AccountDeletionService service) {
    this.service = service;
  }

  @DeleteMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteSelf(Authentication auth) {
    service.deleteUser(auth.getName());
    return ResponseEntity.noContent().build();
  }
}
