package com.pdfmaster.pdfocr.adapter.out.tesseract;

import com.pdfmaster.pdfocr.application.port.out.OcrEngine;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Real Tess4J-backed OCR engine. Disabled by default (see {@code pdfocr.engine}) because
 * production deployment sets {@code pdfocr.engine=tesseract}; tests use the {@link NoopOcrEngine}.
 *
 * <p>Pipeline: PDFBox PDFRenderer rasterizes each page at 300 DPI → Tess4J extracts text →
 * results are returned as plain text, JSON with bounding boxes, or a searchable PDF with an
 * invisible text layer.
 */
@Component
@ConditionalOnProperty(prefix = "pdfocr", name = "engine", havingValue = "tesseract")
public class TesseractOcrEngine implements OcrEngine {

  private static final float OCR_DPI = 300f;

  private final TesseractClient tesseract;

  public TesseractOcrEngine(TesseractClient tesseract) {
    this.tesseract = tesseract;
  }

  @Override
  public String extractText(byte[] pdfBytes, String language) {
    List<PageResult> pages = rasterizeAndOcr(pdfBytes, language);
    StringBuilder sb = new StringBuilder();
    for (PageResult p : pages) {
      sb.append(p.text());
      if (!p.text().isEmpty() && !p.text().endsWith("\n")) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  @Override
  public List<PageResult> extractWithBounds(byte[] pdfBytes, String language) {
    return rasterizeAndOcr(pdfBytes, language);
  }

  @Override
  public byte[] createSearchablePdf(byte[] pdfBytes, String language) {
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(pdfBytes)))) {
      PDFRenderer renderer = new PDFRenderer(doc);
      PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
      for (int i = 0; i < doc.getNumberOfPages(); i++) {
        BufferedImage img = renderer.renderImageWithDPI(i, OCR_DPI, ImageType.RGB);
        String text = runOcr(img, language);
        PDPage page = doc.getPage(i);
        String safe = sanitize(text);
        if (!safe.isEmpty()) {
          appendInvisibleText(doc, page, font, safe);
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return out.toByteArray();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to create searchable PDF", e);
    }
  }

  private static void appendInvisibleText(
      PDDocument doc, PDPage page, PDType1Font font, String text) throws IOException {
    // Rendering mode NEITHER (3) = text is invisible but present in the PDF content stream,
    // making it discoverable by search and copy/paste in PDF readers.
    try (PDPageContentStream cs =
        new PDPageContentStream(doc, page, AppendMode.APPEND, true, true)) {
      cs.beginText();
      cs.setFont(font, 8f);
      cs.setRenderingMode(RenderingMode.NEITHER);
      cs.newLineAtOffset(0, page.getMediaBox().getHeight() - 10);
      cs.showText(text.length() > 2000 ? text.substring(0, 2000) : text);
      cs.endText();
    }
  }

  private List<PageResult> rasterizeAndOcr(byte[] pdfBytes, String language) {
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(pdfBytes)))) {
      PDFRenderer renderer = new PDFRenderer(doc);
      List<PageResult> results = new ArrayList<>(doc.getNumberOfPages());
      for (int i = 0; i < doc.getNumberOfPages(); i++) {
        BufferedImage img = renderer.renderImageWithDPI(i, OCR_DPI, ImageType.RGB);
        String text = runOcr(img, language);
        results.add(new PageResult(i + 1, text, List.of()));
      }
      return results;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to rasterize PDF for OCR", e);
    }
  }

  private String runOcr(BufferedImage img, String language) {
    try {
      return tesseract.ocr(img, language);
    } catch (TesseractException e) {
      throw new IllegalStateException("Tesseract OCR failed", e);
    }
  }

  /** Strip non-WinAnsi characters that PDFBox Type1 fonts cannot encode. */
  private static String sanitize(String text) {
    if (text == null) return "";
    return text.replaceAll("[^\\x20-\\x7E\\n\\r\\t]", " ").trim();
  }
}
