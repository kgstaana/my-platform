
package org.vegetabs.iamservice.verification.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vegetabs.iamservice.verification.adapter.in.rest.dto.VerifyEmailRequest;
import org.vegetabs.iamservice.verification.application.in.VerifyEmailUseCase;

@RestController
@RequestMapping("/v1/verifications/email")
@RequiredArgsConstructor
public class VerifyEmailController {

    private final VerifyEmailUseCase verifyEmailUseCase;

    @PostMapping("/confirm")
    public ResponseEntity<Void> verifyEmail(
        @Valid @RequestBody VerifyEmailRequest request
    ) {
        verifyEmailUseCase.verify(request.token());
        return ResponseEntity.noContent().build();
    }
}
