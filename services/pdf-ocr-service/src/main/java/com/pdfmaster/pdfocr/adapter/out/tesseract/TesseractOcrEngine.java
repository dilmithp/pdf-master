package com.pdfmaster.pdfocr.adapter.out.tesseract;

import com.pdfmaster.pdfocr.application.port.out.OcrEngine;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Real Tess4J-backed OCR engine. Disabled by default (see {@code pdfocr.engine}) because production
 * deployment will set {@code pdfocr.engine=tesseract}; tests use the {@link
 * com.pdfmaster.pdfocr.adapter.out.tesseract.NoopOcrEngine} so they don't require Tesseract native
 * libraries on the host.
 */
@Component
@ConditionalOnProperty(prefix = "pdfocr", name = "engine", havingValue = "tesseract")
public class TesseractOcrEngine implements OcrEngine {

  private final TesseractClient tesseract;

  public TesseractOcrEngine(TesseractClient tesseract) {
    this.tesseract = tesseract;
  }

  @Override
  public String extractText(byte[] pdfBytes, String language) {
    try {
      // PDF rendering to images is performed by PDFBox in the listener; this engine accepts a
      // pre-rendered image when called directly. For the scaffolding the listener only calls
      // through to the NoopOcrEngine — wiring up PDFBox rasterization is left for the
      // implementation
      // ticket that follows this scaffold.
      BufferedImage image = ImageIO.read(new ByteArrayInputStream(pdfBytes));
      if (image == null) {
        throw new IllegalArgumentException("OCR engine received non-image bytes");
      }
      return tesseract.ocr(image, language);
    } catch (IOException | TesseractException e) {
      throw new IllegalStateException("Tesseract OCR failed", e);
    }
  }
}
