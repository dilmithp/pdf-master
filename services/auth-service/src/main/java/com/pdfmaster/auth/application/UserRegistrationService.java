package com.pdfmaster.auth.application;

import com.pdfmaster.auth.domain.User;
import com.pdfmaster.auth.domain.UserStatus;
import java.time.Clock;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use case: register a new user with a hashed password. */
@Service
public class UserRegistrationService {

  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final Clock clock;

  public UserRegistrationService(
      UserRepository userRepository, PasswordHasher passwordHasher, Clock clock) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.clock = clock;
  }

  @Transactional
  public User register(String email, String plainPassword) {
    String normalized = email.trim().toLowerCase(Locale.ROOT);
    if (userRepository.existsByEmail(normalized)) {
      throw new EmailAlreadyRegisteredException(normalized);
    }
    Instant now = Instant.now(clock);
    User user = new User(UUID.randomUUID(), normalized, UserStatus.PENDING_VERIFICATION, now, now);
    return userRepository.save(user, passwordHasher.hash(plainPassword));
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(UUID id) {
    return userRepository.findById(id);
  }
}
