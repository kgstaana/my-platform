package org.vegetabs.iamservice.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vegetabs.iamservice.user.application.in.VerifyUserEmailUseCase;
import org.vegetabs.iamservice.user.application.out.UserRepositoryPort;
import org.vegetabs.iamservice.user.domain.exception.UserNotFoundException;
import org.vegetabs.iamservice.user.domain.model.User;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyUserEmailService implements VerifyUserEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void verifyEmail(UUID userId) {
        log.info("Verifying user email. userId={}", userId);

        User user = userRepositoryPort
            .findById(userId)
            .orElseThrow(UserNotFoundException::new);

        User verifiedUser = user.verifyEmail(Instant.now());

        userRepositoryPort.save(verifiedUser);

        log.info("User email verified successfully. userId={}", userId);
    }
}
