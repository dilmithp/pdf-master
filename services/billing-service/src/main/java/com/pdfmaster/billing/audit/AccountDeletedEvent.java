package com.pdfmaster.billing.audit;

import java.time.Instant;

/** Cascade event received when a user exercises the GDPR right to erasure. */
public record AccountDeletedEvent(String userId, Instant requestedAt) {}
