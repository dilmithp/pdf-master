package com.pdfmaster.pdfcore.adapter.in;

import java.nio.charset.StandardCharsets;

/** Shared magic-byte validation for all PDF listeners. */
final class PdfMagicValidator {

  private static final byte[] PDF_MAGIC = "%PDF-".getBytes(StandardCharsets.US_ASCII);

  private PdfMagicValidator() {}

  static void validate(String key, byte[] bytes) {
    if (bytes.length < PDF_MAGIC.length) {
      throw new IllegalArgumentException("Input too short to be a PDF (magic-byte): " + key);
    }
    for (int i = 0; i < PDF_MAGIC.length; i++) {
      if (bytes[i] != PDF_MAGIC[i]) {
        throw new IllegalArgumentException("Input is not a PDF (magic-byte mismatch): " + key);
      }
    }
  }
}
