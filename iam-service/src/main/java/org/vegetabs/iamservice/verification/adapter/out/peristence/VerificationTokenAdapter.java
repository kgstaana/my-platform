package org.vegetabs.iamservice.verification.adapter.out.peristence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.vegetabs.iamservice.verification.adapter.out.peristence.entity.VerificationTokenEntity;
import org.vegetabs.iamservice.verification.adapter.out.peristence.mapper.VerificationTokenMapper;
import org.vegetabs.iamservice.verification.adapter.out.peristence.repository.VerificationTokenJpaRepository;
import org.vegetabs.iamservice.verification.application.out.VerificationTokenRepositoryPort;
import org.vegetabs.iamservice.verification.domain.model.VerificationToken;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class VerificationTokenAdapter implements VerificationTokenRepositoryPort {

    private final VerificationTokenJpaRepository jpaRepository;
    private final VerificationTokenMapper mapper;

    @Override
    public VerificationToken save(VerificationToken token) {
        VerificationTokenEntity entity = mapper.toEntity(token);
        VerificationTokenEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<VerificationToken> findByTokenHash(String tokenHash) {
        return Optional.empty();
    }
}