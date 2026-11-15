package org.vegetabs.iamservice.verification.application.in;

import org.vegetabs.iamservice.verification.domain.valueobject.VerificationType;

import java.util.UUID;

public interface CreateVerificationTokenUseCase {

    String create(UUID userId, VerificationType type);
}