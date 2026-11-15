package org.vegetabs.iamservice.user.application.out;

public interface PasswordHashingPort {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String passwordHash);
}