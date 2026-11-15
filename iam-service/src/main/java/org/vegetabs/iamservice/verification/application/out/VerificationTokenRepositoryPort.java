package org.vegetabs.iamservice.verification.application.out;

import org.vegetabs.iamservice.verification.domain.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepositoryPort {

    VerificationToken save(VerificationToken token);

    Optional<VerificationToken> findByTokenHash(String tokenHash);
}