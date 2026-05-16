package com.pdfmaster.pdfconvert.audit;

import java.time.Instant;

public record AccountDeletedEvent(String userId, Instant requestedAt) {}
