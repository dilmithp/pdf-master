package com.pdfmaster.auth.application;

/** Outbound port for password hashing. Implemented in {@code config} by a BCrypt-backed bean. */
public interface PasswordHasher {

  String hash(String plain);
}
