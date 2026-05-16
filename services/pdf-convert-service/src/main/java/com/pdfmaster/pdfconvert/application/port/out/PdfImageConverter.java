package com.pdfmaster.pdfconvert.application.port.out;

import com.pdfmaster.pdfconvert.domain.ConvertFormat;

/**
 * Outbound port for PDF↔image conversions that are handled natively via PDFBox + ImageIO rather
 * than the LibreOffice CLI. Supports:
 * <ul>
 *   <li>PDF → JPG (all pages as a ZIP of images)</li>
 *   <li>PDF → PNG (all pages as a ZIP of images)</li>
 *   <li>PDF → TXT (plain text via PDFTextStripper)</li>
 *   <li>JPG → PDF</li>
 *   <li>PNG → PDF</li>
 * </ul>
 */
public interface PdfImageConverter {

  /** Returns true when this converter handles the given from→to pair. */
  boolean supports(ConvertFormat from, ConvertFormat to);

  DocumentConverter.ConversionResult convert(byte[] input, ConvertFormat from, ConvertFormat to);
}
