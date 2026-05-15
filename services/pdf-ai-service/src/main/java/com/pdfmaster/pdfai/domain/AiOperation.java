package com.pdfmaster.pdfai.domain;

import java.util.Locale;

public enum AiOperation {
  CHAT,
  SUMMARIZE,
  TRANSLATE,
  REDACT;

  public static AiOperation parse(String raw) {
    if (raw == null) {
      throw new IllegalArgumentException("operation must not be null");
    }
    return AiOperation.valueOf(raw.toUpperCase(Locale.ROOT));
  }
}
