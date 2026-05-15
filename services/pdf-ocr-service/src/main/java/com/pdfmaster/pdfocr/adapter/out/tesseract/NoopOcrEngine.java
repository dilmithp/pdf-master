package com.pdfmaster.pdfocr.adapter.out.tesseract;

import com.pdfmaster.pdfocr.application.port.out.OcrEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Default OCR engine used when {@code pdfocr.engine} is not set to {@code tesseract}. Returns a
 * deterministic empty result so the rest of the pipeline (job state transitions, S3 upload, etc.)
 * is exercised end-to-end without requiring native libraries.
 */
@Component
@ConditionalOnProperty(
    prefix = "pdfocr",
    name = "engine",
    havingValue = "noop",
    matchIfMissing = true)
public class NoopOcrEngine implements OcrEngine {

  private static final Logger LOG = LoggerFactory.getLogger(NoopOcrEngine.class);

  @Override
  public String extractText(byte[] pdfBytes, String language) {
    LOG.info("NoopOcrEngine.extractText invoked (lang={}, bytes={})", language, pdfBytes.length);
    return "";
  }
}
