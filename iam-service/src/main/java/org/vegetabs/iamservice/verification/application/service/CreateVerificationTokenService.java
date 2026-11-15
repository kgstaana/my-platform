package org.vegetabs.iamservice.verification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vegetabs.iamservice.verification.application.in.CreateVerificationTokenUseCase;
import org.vegetabs.iamservice.verification.application.out.VerificationTokenRepositoryPort;
import org.vegetabs.iamservice.verification.domain.model.VerificationToken;
import org.vegetabs.iamservice.verification.domain.valueobject.VerificationType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateVerificationTokenService implements CreateVerificationTokenUseCase {

    private static final int TOKEN_BYTE_LENGTH = 32;
    private static final long TOKEN_EXPIRATION_MINUTES = 30;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final VerificationTokenRepositoryPort verificationTokenRepositoryPort;

    @Override
    public String create(UUID userId, VerificationType type) {
        String rawToken = generateRawToken();
        String tokenHash = hashToken(rawToken);

        Instant expiresAt = Instant.now().plusSeconds(TOKEN_EXPIRATION_MINUTES * 60);

        VerificationToken token = VerificationToken.create(
            userId,
            tokenHash,
            type,
            expiresAt
        );

        verificationTokenRepositoryPort.save(token);

        return rawToken;
    }

    private String generateRawToken() {
        byte[] tokenBytes = new byte[TOKEN_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(tokenBytes);
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(tokenBytes);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(
                rawToken.getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                "SHA-256 algorithm is not available",
                exception
            );
        }
    }
}