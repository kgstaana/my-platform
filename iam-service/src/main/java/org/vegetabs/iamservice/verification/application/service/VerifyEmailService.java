package org.vegetabs.iamservice.verification.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vegetabs.iamservice.verification.application.in.VerifyEmailUseCase;
import org.vegetabs.iamservice.verification.application.out.VerificationTokenRepositoryPort;
import org.vegetabs.iamservice.verification.application.out.VerifyUserEmailPort;
import org.vegetabs.iamservice.verification.domain.exception.InvalidVerificationTokenException;
import org.vegetabs.iamservice.verification.domain.exception.TokenAlreadyVerifiedException;
import org.vegetabs.iamservice.verification.domain.exception.VerificationTokenExpiredException;
import org.vegetabs.iamservice.verification.domain.model.VerificationToken;
import org.vegetabs.iamservice.verification.domain.valueobject.VerificationType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyEmailService implements VerifyEmailUseCase {

    private final VerificationTokenRepositoryPort verificationTokenRepositoryPort;
    private final VerifyUserEmailPort verifyUserEmailPort;

    @Override
    @Transactional
    public void verify(String rawToken) {
        log.info("Starting email verification");

        String tokenHash = hashToken(rawToken);

        VerificationToken token = verificationTokenRepositoryPort
            .findByTokenHash(tokenHash)
            .orElseThrow(InvalidVerificationTokenException::new);

        if (token.type() != VerificationType.EMAIL_VERIFICATION) {
            throw new InvalidVerificationTokenException();
        }

        if (token.isVerified()) {
            log.info("Verification token already used. userId={}", token.userId());
            throw new TokenAlreadyVerifiedException();
        }

        if (token.isRevoked()) {
            log.warn("Revoked verification token used. userId={}", token.userId());
            throw new InvalidVerificationTokenException();
        }

        if (token.isExpired(Instant.now())) {
            log.info("Expired verification token used. userId={}", token.userId());
            throw new VerificationTokenExpiredException();
        }

        VerificationToken verifiedToken = token.verify(Instant.now());

        verificationTokenRepositoryPort.save(verifiedToken);

        verifyUserEmailPort.verifyEmail(token.userId());

        log.info("Email verification completed. userId={}", token.userId());
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
