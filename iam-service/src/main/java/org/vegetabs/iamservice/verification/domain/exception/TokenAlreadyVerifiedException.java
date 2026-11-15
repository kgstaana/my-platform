package org.vegetabs.iamservice.verification.domain.exception;

public class TokenAlreadyVerifiedException extends RuntimeException {
    public TokenAlreadyVerifiedException() { super("Verification token already used"); }
}
