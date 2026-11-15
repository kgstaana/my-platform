package org.vegetabs.iamservice.user.application.out;

import org.vegetabs.iamservice.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(UUID userId);
}