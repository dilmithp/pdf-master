package com.pdfmaster.pdfconvert.application.port.out;

import com.pdfmaster.pdfconvert.domain.ConvertFormat;

/** Outbound port for converting a document between supported formats. */
public interface DocumentConverter {

  ConversionResult convert(byte[] input, ConvertFormat from, ConvertFormat to);

  /** Bytes + the content-type for the converted artefact. */
  record ConversionResult(byte[] bytes, String contentType) {}
}
