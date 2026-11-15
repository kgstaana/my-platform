package org.vegetabs.iamservice.verification.application.out;

import java.util.UUID;

public interface VerifyUserEmailPort {

    void verifyEmail(UUID userId);

}