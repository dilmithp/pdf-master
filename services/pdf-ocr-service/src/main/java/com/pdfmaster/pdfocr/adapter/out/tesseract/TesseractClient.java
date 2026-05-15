package com.pdfmaster.pdfocr.adapter.out.tesseract;

import com.pdfmaster.pdfocr.config.TesseractProperties;
import java.awt.image.BufferedImage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

/**
 * Thin wrapper around Tess4J's {@link Tesseract}. Tess4J needs Tesseract native libraries on the
 * host; in unit tests this client is mocked.
 */
@Component
public class TesseractClient {

  private final TesseractProperties properties;

  public TesseractClient(TesseractProperties properties) {
    this.properties = properties;
  }

  /** Runs OCR on a single image. */
  public String ocr(BufferedImage image, String language) throws TesseractException {
    ITesseract tesseract = new Tesseract();
    tesseract.setDatapath(properties.dataPath());
    tesseract.setLanguage(language);
    tesseract.setPageSegMode(properties.pageSegMode());
    return tesseract.doOCR(image);
  }
}
