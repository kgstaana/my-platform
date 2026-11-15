package org.vegetabs.iamservice.verification.application.in;

public interface VerifyEmailUseCase {

    void verify(String rawToken);
}