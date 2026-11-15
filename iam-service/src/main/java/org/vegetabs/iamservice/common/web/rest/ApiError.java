package org.vegetabs.iamservice.common.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.vegetabs.iamservice.common.error.ErrorCode;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
    String errorCode,
    String message,
    List<FieldError> errors,
    String traceId,
    Instant timestamp
) {

    public record FieldError(String field, String code, String message) {}

    public static ApiError of(ErrorCode code, String traceId) {
        return new ApiError(code.code(), code.message(), null, traceId, Instant.now());
    }

    public static ApiError of(ErrorCode code, String message, String traceId) {
        return new ApiError(code.code(), message, null, traceId, Instant.now());
    }

    public static ApiError of(ErrorCode code, List<FieldError> errors, String traceId) {
        return new ApiError(code.code(), code.message(), errors, traceId, Instant.now());
    }
}