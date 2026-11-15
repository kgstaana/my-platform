package org.vegetabs.iamservice.user.domain.exception;

import org.vegetabs.iamservice.common.error.DomainException;
import org.vegetabs.iamservice.common.error.ErrorCode;

public class EmailAlreadyExistsException extends DomainException {

    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}