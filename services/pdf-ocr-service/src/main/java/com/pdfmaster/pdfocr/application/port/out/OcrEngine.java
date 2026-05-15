package com.pdfmaster.pdfocr.application.port.out;

/** Outbound port for OCR engines. */
public interface OcrEngine {

  /** Returns the extracted plain text for the given PDF input. */
  String extractText(byte[] pdfBytes, String language);
}
