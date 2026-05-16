package com.pdfmaster.pdfai.adapter.out.pdf;

import com.pdfmaster.pdfai.application.port.out.TextExtractor;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** PDFBox 3.x backed implementation of {@link TextExtractor}. */
@Component
public class PdfBoxTextExtractor implements TextExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(PdfBoxTextExtractor.class);

  @Override
  public String extract(byte[] pdfBytes) {
    if (pdfBytes == null || pdfBytes.length == 0) {
      throw new IllegalArgumentException("PDF bytes must not be null or empty");
    }
    try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
      PDFTextStripper stripper = new PDFTextStripper();
      String text = stripper.getText(doc);
      LOG.debug("PdfBoxTextExtractor: pages={} chars={}", doc.getNumberOfPages(), text.length());
      return text == null ? "" : text;
    } catch (IOException ex) {
      throw new IllegalArgumentException("Failed to extract text from PDF", ex);
    }
  }
}
