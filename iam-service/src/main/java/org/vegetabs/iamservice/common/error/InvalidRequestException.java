package org.vegetabs.iamservice.common.error;

public class InvalidRequestException extends DomainException {

    public InvalidRequestException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
    }
}