package org.vegetabs.iamservice.verification.adapter.out.peristence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vegetabs.iamservice.user.application.in.VerifyUserEmailUseCase;
import org.vegetabs.iamservice.verification.application.out.VerifyUserEmailPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VerifyUserEmailAdapter implements VerifyUserEmailPort {

    private final VerifyUserEmailUseCase verifyUserEmailUseCase;

    @Override
    public void verifyEmail(UUID userId) {
        verifyUserEmailUseCase.verifyEmail(userId);
    }
}
