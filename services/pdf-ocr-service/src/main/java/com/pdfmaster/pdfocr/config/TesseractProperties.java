package com.pdfmaster.pdfocr.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "tesseract")
public record TesseractProperties(@NotBlank String dataPath, @Min(0) int pageSegMode) {

  public TesseractProperties {
    if (dataPath == null || dataPath.isBlank()) {
      dataPath = "/usr/share/tesseract-ocr/4.00/tessdata";
    }
    if (pageSegMode <= 0) {
      pageSegMode = 1;
    }
  }
}
