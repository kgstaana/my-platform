package org.vegetabs.iamservice.user.application.in;

import java.util.UUID;

public interface VerifyUserEmailUseCase {

    void verifyEmail(UUID userId);
}
