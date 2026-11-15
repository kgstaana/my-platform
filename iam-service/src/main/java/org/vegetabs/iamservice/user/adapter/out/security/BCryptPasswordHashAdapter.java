package org.vegetabs.iamservice.user.adapter.out.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.vegetabs.iamservice.user.application.out.PasswordHashingPort;

@Component
public class BCryptPasswordHashAdapter implements PasswordHashingPort {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}