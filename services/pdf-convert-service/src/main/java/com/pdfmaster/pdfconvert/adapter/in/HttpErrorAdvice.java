package com.pdfmaster.pdfconvert.adapter.in;

import java.time.Instant;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HttpErrorAdvice {

  private static final Logger LOG = LoggerFactory.getLogger(HttpErrorAdvice.class);

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorBody> badRequest(IllegalArgumentException e) {
    return ResponseEntity.badRequest()
        .body(new ErrorBody("bad_request", e.getMessage(), Instant.now()));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorBody> notFound(NoSuchElementException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorBody("not_found", e.getMessage(), Instant.now()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorBody> internal(RuntimeException e) {
    LOG.error("Unhandled service error", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorBody("internal_error", "An unexpected error occurred.", Instant.now()));
  }

  public record ErrorBody(String code, String message, Instant timestamp) {}
}
