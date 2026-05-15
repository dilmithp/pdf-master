package com.pdfmaster.auth.adapter.out.persistence;

import com.pdfmaster.auth.application.UserRepository;
import com.pdfmaster.auth.domain.User;
import com.pdfmaster.auth.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Adapts the {@link UserJpaRepository} to the application-layer {@link UserRepository} port. */
@Repository
public class UserRepositoryAdapter implements UserRepository {

  private final UserJpaRepository jpa;

  public UserRepositoryAdapter(UserJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public User save(User user, String passwordHash) {
    UserEntity entity =
        new UserEntity(
            user.id(),
            user.email(),
            passwordHash,
            UserStatusEntity.valueOf(user.status().name()),
            user.createdAt(),
            user.updatedAt());
    UserEntity saved = jpa.save(entity);
    return toDomain(saved);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return jpa.findById(id).map(UserRepositoryAdapter::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpa.findByEmail(email).map(UserRepositoryAdapter::toDomain);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpa.existsByEmail(email);
  }

  private static User toDomain(UserEntity e) {
    return new User(
        e.getId(),
        e.getEmail(),
        UserStatus.valueOf(e.getStatus().name()),
        e.getCreatedAt(),
        e.getUpdatedAt());
  }
}
