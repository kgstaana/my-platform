package org.vegetabs.iamservice.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.vegetabs.iamservice.user.application.in.RegisterUserUseCase;
import org.vegetabs.iamservice.user.application.out.EmailVerificationPort;
import org.vegetabs.iamservice.user.application.out.PasswordHashingPort;
import org.vegetabs.iamservice.user.application.out.UserRepositoryPort;
import org.vegetabs.iamservice.user.domain.exception.EmailAlreadyExistsException;
import org.vegetabs.iamservice.user.domain.model.User;
import org.vegetabs.iamservice.user.domain.valueobject.Email;
import org.vegetabs.iamservice.verification.application.in.CreateVerificationTokenUseCase;
import org.vegetabs.iamservice.verification.domain.valueobject.VerificationType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final PasswordHashingPort passwordHashingPort;
    private final UserRepositoryPort userRepositoryPort;
    private final CreateVerificationTokenUseCase createVerificationTokenUseCase;
    private final EmailVerificationPort emailVerificationPort;

    private final TransactionTemplate transactionTemplate;
    private record Registration(User user, String rawToken) {}

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

        // user and token commit or roll back together
        Registration registration = transactionTemplate.execute(status -> {
            User savedUser = userRepositoryPort.save(user);
            String rawToken = createVerificationTokenUseCase.create(
                savedUser.id(),
                VerificationType.EMAIL_VERIFICATION
            );
            return new Registration(savedUser, rawToken);
        });

        try {
            emailVerificationPort.send(registration.user(), registration.rawToken());
        } catch (RuntimeException ex) {
            log.warn("Verification email failed, userId={}", registration.user().id(), ex);
        }

        log.info("User registration completed. userId={}", registration.user().id());

        return registration.user();
    }
}
