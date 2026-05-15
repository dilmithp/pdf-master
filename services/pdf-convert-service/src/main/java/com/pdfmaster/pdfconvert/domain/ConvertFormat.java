package com.pdfmaster.pdfconvert.domain;

import java.util.Locale;
import java.util.Set;

/**
 * Supported conversion endpoints. The matrix is intentionally small and explicit to keep the
 * LibreOffice command surface predictable.
 */
public enum ConvertFormat {
  PDF("application/pdf"),
  DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
  XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
  PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
  ODT("application/vnd.oasis.opendocument.text"),
  PNG("image/png"),
  JPG("image/jpeg");

  private static final Set<String> ALLOWED_INPUTS = Set.of("pdf", "docx", "xlsx", "pptx", "odt");

  private final String contentType;

  ConvertFormat(String contentType) {
    this.contentType = contentType;
  }

  public String contentType() {
    return contentType;
  }

  public String extension() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static ConvertFormat parse(String raw) {
    if (raw == null) {
      throw new IllegalArgumentException("format must not be null");
    }
    return ConvertFormat.valueOf(raw.toUpperCase(Locale.ROOT));
  }

  public static boolean isAllowedInput(String raw) {
    return raw != null && ALLOWED_INPUTS.contains(raw.toLowerCase(Locale.ROOT));
  }
}
