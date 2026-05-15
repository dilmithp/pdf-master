package com.pdfmaster.notification.application;

/** Thrown when a requested template + locale tuple does not exist. */
public class TemplateNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TemplateNotFoundException(String code, String locale) {
    super("Template not found: code=" + code + " locale=" + locale);
  }
}
