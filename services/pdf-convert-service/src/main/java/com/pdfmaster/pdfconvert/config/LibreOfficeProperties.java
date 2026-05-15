package com.pdfmaster.pdfconvert.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "libreoffice")
public record LibreOfficeProperties(@NotBlank String binary, @Min(5) int timeoutSeconds) {

  public LibreOfficeProperties {
    if (binary == null || binary.isBlank()) {
      binary = "libreoffice";
    }
    if (timeoutSeconds <= 0) {
      timeoutSeconds = 60;
    }
  }
}
