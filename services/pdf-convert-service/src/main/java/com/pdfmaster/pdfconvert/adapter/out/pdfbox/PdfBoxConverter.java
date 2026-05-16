package com.pdfmaster.pdfconvert.adapter.out.pdfbox;

import com.pdfmaster.pdfconvert.application.port.out.DocumentConverter;
import com.pdfmaster.pdfconvert.application.port.out.PdfImageConverter;
import com.pdfmaster.pdfconvert.domain.ConvertFormat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

/**
 * PDFBox-backed image converter. Handles: PDF→JPG (ZIP), PDF→PNG (ZIP), PDF→TXT,
 * JPG→PDF, PNG→PDF.
 */
@Component
public class PdfBoxConverter implements PdfImageConverter {

  private static final Set<ConvertFormat> IMAGE_FORMATS = Set.of(ConvertFormat.JPG, ConvertFormat.PNG);
  private static final float DEFAULT_DPI = 150f;

  @Override
  public boolean supports(ConvertFormat from, ConvertFormat to) {
    if (from == ConvertFormat.PDF && (IMAGE_FORMATS.contains(to) || to == ConvertFormat.TXT)) {
      return true;
    }
    return IMAGE_FORMATS.contains(from) && to == ConvertFormat.PDF;
  }

  @Override
  public DocumentConverter.ConversionResult convert(byte[] input, ConvertFormat from, ConvertFormat to) {
    try {
      if (from == ConvertFormat.PDF && IMAGE_FORMATS.contains(to)) {
        return pdfToImages(input, to);
      }
      if (from == ConvertFormat.PDF && to == ConvertFormat.TXT) {
        return pdfToText(input);
      }
      if (IMAGE_FORMATS.contains(from) && to == ConvertFormat.PDF) {
        return imageToPdf(input, from);
      }
      throw new IllegalArgumentException("Unsupported conversion: " + from + " -> " + to);
    } catch (IOException e) {
      throw new IllegalStateException("PDFBox conversion failed: " + from + " -> " + to, e);
    }
  }

  private DocumentConverter.ConversionResult pdfToImages(byte[] input, ConvertFormat imgFormat)
      throws IOException {
    String formatName = imgFormat == ConvertFormat.JPG ? "JPEG" : "PNG";
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      PDFRenderer renderer = new PDFRenderer(doc);
      ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
      try (ZipOutputStream zip = new ZipOutputStream(zipOut)) {
        for (int i = 0; i < doc.getNumberOfPages(); i++) {
          BufferedImage img = renderer.renderImageWithDPI(i, DEFAULT_DPI, ImageType.RGB);
          ByteArrayOutputStream imgOut = new ByteArrayOutputStream();
          ImageIO.write(img, formatName, imgOut);
          String ext = imgFormat.extension();
          zip.putNextEntry(new ZipEntry("page_" + (i + 1) + "." + ext));
          zip.write(imgOut.toByteArray());
          zip.closeEntry();
        }
      }
      return new DocumentConverter.ConversionResult(zipOut.toByteArray(), "application/zip");
    }
  }

  private DocumentConverter.ConversionResult pdfToText(byte[] input) throws IOException {
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      PDFTextStripper stripper = new PDFTextStripper();
      String text = stripper.getText(doc);
      return new DocumentConverter.ConversionResult(
          text.getBytes(java.nio.charset.StandardCharsets.UTF_8),
          "text/plain; charset=utf-8");
    }
  }

  private DocumentConverter.ConversionResult imageToPdf(byte[] input, ConvertFormat imgFormat)
      throws IOException {
    BufferedImage img = ImageIO.read(new ByteArrayInputStream(input));
    if (img == null) {
      throw new IllegalArgumentException(
          "Could not decode image as " + imgFormat.name() + ": invalid or corrupt data");
    }
    try (PDDocument doc = new PDDocument()) {
      float width = img.getWidth();
      float height = img.getHeight();
      PDRectangle pageSize = new PDRectangle(width, height);
      PDPage page = new PDPage(pageSize);
      doc.addPage(page);
      String formatName = imgFormat == ConvertFormat.JPG ? "JPEG" : "PNG";
      PDImageXObject ximage = PDImageXObject.createFromByteArray(doc, input, formatName);
      try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
        cs.drawImage(ximage, 0, 0, width, height);
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new DocumentConverter.ConversionResult(out.toByteArray(), "application/pdf");
    }
  }
}
