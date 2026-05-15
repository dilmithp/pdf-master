package com.pdfmaster.notification.adapter.in;

import com.pdfmaster.notification.application.NotificationService;
import com.pdfmaster.notification.application.TemplateNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST adapter exposing an ad-hoc test send endpoint. */
@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

  private final NotificationService service;

  public NotificationController(NotificationService service) {
    this.service = service;
  }

  @PostMapping("/test")
  public ResponseEntity<SendResponse> test(@Valid @RequestBody SendRequest request) {
    String messageId =
        service.send(request.templateCode(), request.locale(), request.recipient(), Map.of());
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SendResponse(messageId));
  }

  @ExceptionHandler(TemplateNotFoundException.class)
  ResponseEntity<ErrorResponse> handleNotFound(TemplateNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("template_not_found"));
  }

  public record SendRequest(
      @NotBlank @Size(max = 128) String templateCode,
      @NotBlank @Size(max = 16) String locale,
      @Email @NotBlank String recipient) {}

  public record SendResponse(String messageId) {}

  public record ErrorResponse(String code) {}
}
