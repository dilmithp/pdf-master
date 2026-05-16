package com.pdfmaster.notification.audit;

import java.time.Instant;

public record AccountDeletedEvent(String userId, Instant requestedAt) {}
