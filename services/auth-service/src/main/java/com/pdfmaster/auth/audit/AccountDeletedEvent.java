package com.pdfmaster.auth.audit;

import java.time.Instant;

/** Broadcast event emitted when a user requests account deletion (GDPR right to erasure). */
public record AccountDeletedEvent(String userId, Instant requestedAt) {}
