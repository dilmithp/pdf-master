package com.pdfmaster.pdfocr.application.port.out;

import java.util.List;

/** Outbound port for OCR engines. */
public interface OcrEngine {

  /**
   * Extracts plain text from the given PDF bytes. Each page is rasterized by the implementation
   * before being fed to the underlying OCR engine.
   *
   * @param pdfBytes raw bytes of the source PDF
   * @param language Tesseract language code (e.g. "eng")
   * @return extracted plain text (all pages concatenated)
   */
  String extractText(byte[] pdfBytes, String language);

  /**
   * Extracts text with bounding-box metadata from the given PDF bytes.
   *
   * @param pdfBytes raw bytes of the source PDF
   * @param language Tesseract language code
   * @return list of word-level bounding boxes per page
   */
  default List<PageResult> extractWithBounds(byte[] pdfBytes, String language) {
    return List.of(new PageResult(1, extractText(pdfBytes, language), List.of()));
  }

  /**
   * Creates a searchable PDF by overlaying an invisible text layer over the original pages.
   *
   * @param pdfBytes raw bytes of the source PDF
   * @param language Tesseract language code
   * @return bytes of the searchable PDF
   */
  default byte[] createSearchablePdf(byte[] pdfBytes, String language) {
    // Default: return the original without a text layer (NoopOcrEngine safe fallback).
    return pdfBytes;
  }

  /** Bounding-box result for a single word on a page. */
  record WordBox(String word, int x, int y, int width, int height) {}

  /** OCR result for one page. */
  record PageResult(int pageNumber, String text, List<WordBox> words) {}
}
