package org.vegetabs.iamservice.user.domain.model;

import org.vegetabs.iamservice.user.domain.exception.AccountAlreadyVerifiedException;
import org.vegetabs.iamservice.user.domain.exception.InvalidAccountStateException;
import org.vegetabs.iamservice.user.domain.valueobject.Email;
import org.vegetabs.iamservice.user.domain.valueobject.UserStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class User {

    private final UUID id;
    private final Email email;
    private final String passwordHash;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final UserStatus status;
    private final Instant emailVerifiedAt;
    private final Instant createdAt;

    private User(
        UUID id,
        Email email,
        String passwordHash,
        String firstName,
        String lastName,
        String phoneNumber,
        UserStatus status,
        Instant emailVerifiedAt,
        Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.passwordHash = passwordHash;
        this.firstName = Objects.requireNonNull(firstName, "firstName");
        this.lastName = Objects.requireNonNull(lastName, "lastName");
        this.phoneNumber = phoneNumber;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.emailVerifiedAt = emailVerifiedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static User register(
        Email email,
        String passwordHash,
        String firstName,
        String lastName,
        String phoneNumber
    ) {
        Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        Objects.requireNonNull(phoneNumber, "phoneNumber must not be null");

        return new User(
            UUID.randomUUID(),
            email,
            passwordHash,
            firstName.trim(),
            lastName.trim(),
            phoneNumber,
            UserStatus.PENDING_VERIFICATION,
            null,
            Instant.now()
        );
    }

    public static User registerSocial(
        Email email,
        String firstName,
        String lastName
    ) {
        Instant now = Instant.now();

        return new User(
            UUID.randomUUID(),
            email,
            null,
            firstName.trim(),
            lastName.trim(),
            null,
            UserStatus.ACTIVE,
            now,
            now
        );
    }

    public User verifyEmail(Instant now) {
        Objects.requireNonNull(now, "now must not be null");

        if (emailVerifiedAt != null) {
            throw new AccountAlreadyVerifiedException();
        }

        if (status != UserStatus.PENDING_VERIFICATION) {
            throw new InvalidAccountStateException();
        }

        return new User(
            id,
            email,
            passwordHash,
            firstName,
            lastName,
            phoneNumber,
            UserStatus.ACTIVE,
            now,
            createdAt
        );
    }

    public static User restore(
        UUID id,
        Email email,
        String passwordHash,
        String firstName,
        String lastName,
        String phoneNumber,
        UserStatus status,
        Instant emailVerifiedAt,
        Instant createdAt
    ) {
        return new User(
            id,
            email,
            passwordHash,
            firstName,
            lastName,
            phoneNumber,
            status,
            emailVerifiedAt,
            createdAt
        );
    }

    public UUID id() { return id; }
    public Email email() { return email; }
    public String passwordHash() { return passwordHash; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public String phoneNumber() { return phoneNumber; }
    public UserStatus status() { return status; }
    public Instant emailVerifiedAt() { return emailVerifiedAt; }
    public Instant createdAt() { return createdAt; }

    @Override
    public String toString() {
        return "User[id=" + id + ", status=" + status + "]";
    }
}