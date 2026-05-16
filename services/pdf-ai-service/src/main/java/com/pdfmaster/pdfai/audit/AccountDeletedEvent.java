package com.pdfmaster.pdfai.audit;

import java.time.Instant;

public record AccountDeletedEvent(String userId, Instant requestedAt) {}
