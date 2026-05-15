package com.pdfmaster.auth.adapter.out.persistence;

/** Persistence-side enum mirror for the domain {@code UserStatus}. */
public enum UserStatusEntity {
  PENDING_VERIFICATION,
  ACTIVE,
  SUSPENDED,
  DEACTIVATED
}
