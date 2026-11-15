package org.vegetabs.iamservice.user.domain.exception;

import org.vegetabs.iamservice.common.error.DomainException;
import org.vegetabs.iamservice.common.error.ErrorCode;

public class AccountAlreadyVerifiedException extends DomainException {

    public AccountAlreadyVerifiedException() {
        super(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
    }
}