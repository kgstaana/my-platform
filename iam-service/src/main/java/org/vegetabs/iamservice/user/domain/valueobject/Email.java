package org.vegetabs.iamservice.user.domain.valueobject;

import org.vegetabs.iamservice.user.domain.exception.InvalidUserDataException;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final int MAX_LENGTH = 254;
    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new InvalidUserDataException("Email is required");
        }
        value = value.trim().toLowerCase(Locale.ROOT);   // normalize, so uniqueness checks are reliable
        if (value.length() > MAX_LENGTH || !PATTERN.matcher(value).matches()) {
            throw new InvalidUserDataException("Email format is invalid");
        }
    }
}