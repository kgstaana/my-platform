package org.vegetabs.iamservice.user.application.in;

import org.vegetabs.iamservice.user.domain.model.User;

public interface RegisterUserUseCase {

    User register(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
    );
}
