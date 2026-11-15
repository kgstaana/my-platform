package org.vegetabs.iamservice.user.domain.exception;

import org.vegetabs.iamservice.common.error.DomainException;
import org.vegetabs.iamservice.common.error.ErrorCode;

public class InvalidAccountStateException extends DomainException {

    public InvalidAccountStateException() {
        super(ErrorCode.INVALID_ACCOUNT_STATE);
    }
}
