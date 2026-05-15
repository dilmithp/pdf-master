package com.pdfmaster.auth.application;

/** Thrown when an attempt is made to register an email that already exists. */
public class EmailAlreadyRegisteredException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public EmailAlreadyRegisteredException(String email) {
    super("Email already registered: " + email);
  }
}
