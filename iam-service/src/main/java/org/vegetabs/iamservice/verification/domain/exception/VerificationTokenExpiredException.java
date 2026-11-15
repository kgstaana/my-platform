package org.vegetabs.iamservice.verification.domain.exception;

public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException() { super("Verification token expired"); }
}
