package org.vegetabs.iamservice.user.adapter.in.rest.dto;

import org.vegetabs.iamservice.user.domain.model.User;
import org.vegetabs.iamservice.user.domain.valueobject.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record RegisterUserResponse(
    UUID id,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    UserStatus status,
    Instant emailVerifiedAt,
    Instant createdAt
) {
    public static RegisterUserResponse from(User user) {
        return new RegisterUserResponse(
            user.id(),
            user.email().value(),
            user.firstName(),
            user.lastName(),
            user.phoneNumber(),
            user.status(),
            user.emailVerifiedAt(),
            user.createdAt()
        );
    }
}