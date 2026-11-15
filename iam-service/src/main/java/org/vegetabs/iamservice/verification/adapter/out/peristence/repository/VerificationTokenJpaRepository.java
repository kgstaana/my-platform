package org.vegetabs.iamservice.verification.adapter.out.peristence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vegetabs.iamservice.verification.adapter.out.peristence.entity.VerificationTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenJpaRepository extends JpaRepository<VerificationTokenEntity, UUID> {

    Optional<VerificationTokenEntity> findByTokenHash(String tokenHash);

    Optional<VerificationTokenEntity> findByUserIdAndType(UUID userId, String type);
}