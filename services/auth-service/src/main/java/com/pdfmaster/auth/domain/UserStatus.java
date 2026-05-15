package com.pdfmaster.auth.domain;

/** Lifecycle status for a {@link User}. */
public enum UserStatus {
  PENDING_VERIFICATION,
  ACTIVE,
  SUSPENDED,
  DEACTIVATED
}
