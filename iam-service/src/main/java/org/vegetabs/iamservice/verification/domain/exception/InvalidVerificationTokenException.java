package org.vegetabs.iamservice.verification.domain.exception;

public class InvalidVerificationTokenException extends RuntimeException {

    public InvalidVerificationTokenException() {
        super("Verification token is invalid or expired");
    }
}