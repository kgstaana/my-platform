package org.vegetabs.iamservice.user.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vegetabs.iamservice.user.adapter.out.persistence.entity.UserEntity;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

}
