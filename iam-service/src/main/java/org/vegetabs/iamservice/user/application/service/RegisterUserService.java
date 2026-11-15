package org.vegetabs.iamservice.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vegetabs.iamservice.user.application.in.RegisterUserUseCase;
import org.vegetabs.iamservice.user.application.out.PasswordHashingPort;
import org.vegetabs.iamservice.user.application.out.UserRepositoryPort;
import org.vegetabs.iamservice.user.domain.exception.EmailAlreadyExistsException;
import org.vegetabs.iamservice.user.domain.model.User;
import org.vegetabs.iamservice.user.domain.valueobject.Email;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final PasswordHashingPort passwordHashingPort;
    private final UserRepositoryPort userRepositoryPort;

    public User register(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
    ) {
        log.info("Starting user registration");

        Email normalizedEmail = new Email(email);

        if (userRepositoryPort.existsByEmail(normalizedEmail.value())) {
            log.warn("User registration failed: email already exists");
            throw new EmailAlreadyExistsException();
        }

        String passwordHash = passwordHashingPort.hash(password);

        User user = User.register(
            normalizedEmail,
            passwordHash,
            firstName,
            lastName,
            phoneNumber
        );

        User savedUser = userRepositoryPort.save(user);

        log.info("User registration completed. userId={}", savedUser.id());

        return user;
    }
}
