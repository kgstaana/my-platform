package org.vegetabs.iamservice.user.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import org.vegetabs.iamservice.user.adapter.out.persistence.entity.UserEntity;
import org.vegetabs.iamservice.user.domain.model.User;
import org.vegetabs.iamservice.user.domain.valueobject.Email;
import org.vegetabs.iamservice.user.domain.valueobject.UserStatus;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setEmail(user.email().value());
        entity.setPasswordHash(user.passwordHash());
        entity.setFirstName(user.firstName());
        entity.setLastName(user.lastName());
        entity.setPhoneNumber(user.phoneNumber());
        entity.setStatus(user.status().name());
        entity.setEmailVerifiedAt(user.emailVerifiedAt());
        entity.setCreatedAt(user.createdAt());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        return User.restore(
            entity.getId(),
            new Email(entity.getEmail()),
            entity.getPasswordHash(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getPhoneNumber(),
            UserStatus.valueOf(entity.getStatus()),
            entity.getEmailVerifiedAt(),
            entity.getCreatedAt()
        );
    }
}