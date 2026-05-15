package com.pdfmaster.auth.application;

import com.pdfmaster.auth.domain.User;
import java.util.Optional;
import java.util.UUID;

/** Outbound port for user persistence. Implemented by an adapter in {@code adapter.out}. */
public interface UserRepository {

  User save(User user, String passwordHash);

  Optional<User> findById(UUID id);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
