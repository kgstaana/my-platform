package org.vegetabs.iamservice.user.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vegetabs.iamservice.common.pagination.Page;
import org.vegetabs.iamservice.common.web.rest.ApiResult;
import org.vegetabs.iamservice.user.adapter.in.rest.dto.RegisterUserRequest;
import org.vegetabs.iamservice.user.adapter.in.rest.dto.RegisterUserResponse;
import org.vegetabs.iamservice.user.application.in.RegisterUserUseCase;
import org.vegetabs.iamservice.user.domain.model.User;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUseCase;

    @GetMapping
    public ResponseEntity<ApiResult<List<String>>> list() {
        List<String> users = List.of("kenji@example.com", "kate@example.com");
        Page<String> page = new Page<>(users, 0, 20, users.size());

        return ResponseEntity.ok(ApiResult.ofPage(page));
    }

    @PostMapping
    public ResponseEntity<ApiResult<RegisterUserResponse>> register(
        @Valid @RequestBody RegisterUserRequest request
    ) {
        log.info("Register user request received");

        User user = registerUseCase.register(
            request.email(),
            request.password(),
            request.firstName(),
            request.lastName(),
            request.phoneNumber()
        );

        log.info("User registered successfully. userId={}", user.id());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResult.of(
                RegisterUserResponse.from(user)
            ));
    }
}
