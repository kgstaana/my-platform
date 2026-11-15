package org.vegetabs.iamservice.user.application.out;

import org.vegetabs.iamservice.user.domain.model.User;

public interface UserRepositoryPort {

    User save(User user);

    boolean existsByEmail(String email);
}