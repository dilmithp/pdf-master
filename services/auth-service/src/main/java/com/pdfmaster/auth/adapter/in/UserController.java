package com.pdfmaster.auth.adapter.in;

import com.pdfmaster.auth.application.EmailAlreadyRegisteredException;
import com.pdfmaster.auth.application.UserRegistrationService;
import com.pdfmaster.auth.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** REST adapter for user registration and lookup. */
@RestController
@RequestMapping("/v1/users")
public class UserController {

  private final UserRegistrationService service;

  public UserController(UserRegistrationService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
    User user = service.register(request.email(), request.password());
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.id())
            .toUri();
    return ResponseEntity.created(location).body(UserResponse.from(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> get(@PathVariable UUID id) {
    return service
        .findById(id)
        .map(UserResponse::from)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  ResponseEntity<ErrorResponse> handleConflict(EmailAlreadyRegisteredException ex) {
    return ResponseEntity.status(409).body(new ErrorResponse("email_already_registered"));
  }

  public record RegisterRequest(
      @Email @NotBlank String email, @NotBlank @Size(min = 12, max = 128) String password) {}

  public record UserResponse(
      UUID id, String email, String status, Instant createdAt, Instant updatedAt) {

    static UserResponse from(User u) {
      return new UserResponse(u.id(), u.email(), u.status().name(), u.createdAt(), u.updatedAt());
    }
  }

  public record ErrorResponse(String code) {}
}
