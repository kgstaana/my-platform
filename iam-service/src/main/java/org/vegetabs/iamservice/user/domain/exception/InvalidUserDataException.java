package org.vegetabs.iamservice.user.domain.exception;

import org.vegetabs.iamservice.common.error.DomainException;
import org.vegetabs.iamservice.common.error.ErrorCode;

public class InvalidUserDataException extends DomainException {

    public InvalidUserDataException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
    }
}