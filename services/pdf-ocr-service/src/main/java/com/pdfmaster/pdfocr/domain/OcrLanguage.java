package com.pdfmaster.pdfocr.domain;

import java.util.Set;

/** Whitelist of Tesseract language codes the service supports. */
public final class OcrLanguage {

  public static final String DEFAULT = "eng";

  private static final Set<String> ALLOWED =
      Set.of("eng", "spa", "fra", "deu", "por", "ita", "nld", "rus", "jpn", "chi_sim", "chi_tra");

  private OcrLanguage() {}

  public static String normalise(String raw) {
    if (raw == null || raw.isBlank()) {
      return DEFAULT;
    }
    String value = raw.toLowerCase(java.util.Locale.ROOT);
    if (!ALLOWED.contains(value)) {
      throw new IllegalArgumentException("Unsupported OCR language: " + raw);
    }
    return value;
  }
}
