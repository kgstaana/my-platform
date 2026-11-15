
package org.vegetabs.iamservice.verification.domain.model;

import org.vegetabs.iamservice.verification.domain.exception.InvalidVerificationTokenException;
import org.vegetabs.iamservice.verification.domain.valueobject.VerificationType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class VerificationToken {

    private final UUID id;
    private final UUID userId;
    private final String tokenHash;
    private final VerificationType type;
    private final Instant expiresAt;
    private final Instant verifiedAt;
    private final Instant revokedAt;
    private final Instant createdAt;

    private VerificationToken(
        UUID id,
        UUID userId,
        String tokenHash,
        VerificationType type,
        Instant expiresAt,
        Instant verifiedAt,
        Instant revokedAt,
        Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.tokenHash = Objects.requireNonNull(tokenHash, "tokenHash must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        this.verifiedAt = verifiedAt;
        this.revokedAt = revokedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");

        if (tokenHash.isBlank()) {
            throw new IllegalArgumentException("tokenHash must not be blank");
        }
    }

    public static VerificationToken create(
        UUID userId,
        String tokenHash,
        VerificationType type,
        Instant expiresAt
    ) {
        return new VerificationToken(
            UUID.randomUUID(),
            userId,
            tokenHash,
            type,
            expiresAt,
            null,
            null,
            Instant.now()
        );
    }

    public boolean isExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    public boolean isVerified() {
        return verifiedAt != null;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isValid(Instant now) {
        return !isExpired(now) && !isVerified() && !isRevoked();
    }

    public VerificationToken verify(Instant now) {
        Objects.requireNonNull(now, "now must not be null");

        if (!isValid(now)) {
            throw new InvalidVerificationTokenException();
        }

        return new VerificationToken(
            id,
            userId,
            tokenHash,
            type,
            expiresAt,
            now,
            revokedAt,
            createdAt
        );
    }

    public VerificationToken revoke(Instant now) {
        Objects.requireNonNull(now, "now must not be null");

        if (isRevoked()) {
            return this;
        }

        return new VerificationToken(
            id,
            userId,
            tokenHash,
            type,
            expiresAt,
            verifiedAt,
            now,
            createdAt
        );
    }

    public static VerificationToken restore(
        UUID id,
        UUID userId,
        String tokenHash,
        VerificationType type,
        Instant expiresAt,
        Instant verifiedAt,
        Instant revokedAt,
        Instant createdAt
    ) {
        return new VerificationToken(
            id,
            userId,
            tokenHash,
            type,
            expiresAt,
            verifiedAt,
            revokedAt,
            createdAt
        );
    }

    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public String tokenHash() { return tokenHash; }
    public VerificationType type() { return type; }
    public Instant expiresAt() { return expiresAt; }
    public Instant verifiedAt() { return verifiedAt; }
    public Instant revokedAt() { return revokedAt; }
    public Instant createdAt() { return createdAt; }

    @Override
    public String toString() {
        return "VerificationToken[id=" + id
            + ", userId=" + userId
            + ", type=" + type
            + ", expiresAt=" + expiresAt
            + "]";
    }
}
