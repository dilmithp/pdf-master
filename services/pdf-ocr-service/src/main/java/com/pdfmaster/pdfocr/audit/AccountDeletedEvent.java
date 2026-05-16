package com.pdfmaster.pdfocr.audit;

import java.time.Instant;

public record AccountDeletedEvent(String userId, Instant requestedAt) {}
