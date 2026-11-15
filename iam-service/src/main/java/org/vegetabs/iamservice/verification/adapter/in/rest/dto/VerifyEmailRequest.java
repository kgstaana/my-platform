package org.vegetabs.iamservice.verification.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(

    @NotBlank(message = "Verification token is required")
    String token

) { }
