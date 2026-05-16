package com.pdfmaster.esign.audit;

import java.time.Instant;

public record AccountDeletedEvent(String userId, Instant requestedAt) {}
