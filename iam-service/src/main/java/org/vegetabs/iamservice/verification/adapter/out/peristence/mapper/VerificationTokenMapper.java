package org.vegetabs.iamservice.verification.adapter.out.peristence.mapper;

import org.springframework.stereotype.Component;
import org.vegetabs.iamservice.verification.adapter.out.peristence.entity.VerificationTokenEntity;
import org.vegetabs.iamservice.verification.domain.model.VerificationToken;
import org.vegetabs.iamservice.verification.domain.valueobject.VerificationType;

@Component
public class VerificationTokenMapper {

    public VerificationTokenEntity toEntity(VerificationToken token) {
        VerificationTokenEntity entity = new VerificationTokenEntity();
        entity.setId(token.id());
        entity.setUserId(token.userId());
        entity.setTokenHash(token.tokenHash());
        entity.setType(token.type().name());
        entity.setExpiresAt(token.expiresAt());
        entity.setVerifiedAt(token.verifiedAt());
        entity.setRevokedAt(token.revokedAt());
        entity.setCreatedAt(token.createdAt());
        return entity;
    }

    public VerificationToken toDomain(VerificationTokenEntity entity) {
        return VerificationToken.restore(
            entity.getId(),
            entity.getUserId(),
            entity.getTokenHash(),
            VerificationType.valueOf(entity.getType()),
            entity.getExpiresAt(),
            entity.getVerifiedAt(),
            entity.getRevokedAt(),
            entity.getCreatedAt()
        );
    }
}