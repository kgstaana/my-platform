
package org.vegetabs.iamservice.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.vegetabs.iamservice.user.adapter.out.persistence.entity.UserEntity;
import org.vegetabs.iamservice.user.adapter.out.persistence.mapper.UserMapper;
import org.vegetabs.iamservice.user.adapter.out.persistence.repository.UserJpaRepository;
import org.vegetabs.iamservice.user.application.out.UserRepositoryPort;
import org.vegetabs.iamservice.user.domain.model.User;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
