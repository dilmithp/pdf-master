package com.pdfmaster.pdfai.application.port.out;

/**
 * Outbound port for extracting plain text from a PDF byte array. Implementation: {@code
 * PdfBoxTextExtractor}.
 */
public interface TextExtractor {

  /**
   * Extract all readable text from the PDF.
   *
   * @param pdfBytes raw PDF bytes; must start with the {@code %PDF-} magic bytes
   * @return extracted plain text (never null, may be empty for image-only PDFs)
   * @throws IllegalArgumentException if the bytes are not a valid PDF
   */
  String extract(byte[] pdfBytes);
}
