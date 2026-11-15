package org.vegetabs.iamservice.user.domain.exception;

import org.vegetabs.iamservice.common.error.DomainException;
import org.vegetabs.iamservice.common.error.ErrorCode;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}