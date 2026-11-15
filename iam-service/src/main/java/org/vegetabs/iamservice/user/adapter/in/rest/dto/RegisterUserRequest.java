package org.vegetabs.iamservice.user.adapter.in.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Size(max = 254, message = "Email must be at most 254 characters")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 12, max = 72, message = "Password must be between 12 and 72 characters")
    String password,

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    String lastName,

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Phone number must be in international format, e.g. +639171234567")
    String phoneNumber
) {

    /** Keeps the password out of logs if the request is ever printed. */
    @Override
    public String toString() {
        return "RegisterUserRequest[email=" + email + ", password=[PROTECTED], firstName=" + firstName
            + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + "]";
    }
}