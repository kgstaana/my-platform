package org.vegetabs.iamservice.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 - Bad Request
    VALIDATION_FAILED("IAM-400-001", "Validation failed", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("IAM-400-002", "Malformed or invalid request", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("IAM-400-003", "Required field is missing", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("IAM-400-004", "Invalid parameter", HttpStatus.BAD_REQUEST),

    // 401 - Unauthorized
    INVALID_CREDENTIALS("IAM-401-001", "Invalid email or password", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("IAM-401-002", "Authentication required", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("IAM-401-003", "Invalid or expired token", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("IAM-401-004", "Token has expired", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("IAM-401-005", "Invalid refresh token", HttpStatus.UNAUTHORIZED),

    // 403 - Forbidden
    FORBIDDEN("IAM-403-001", "Access denied", HttpStatus.FORBIDDEN),
    INSUFFICIENT_PERMISSIONS("IAM-403-002", "Insufficient permissions", HttpStatus.FORBIDDEN),
    ACCOUNT_SUSPENDED("IAM-403-003", "Account is suspended", HttpStatus.FORBIDDEN),
    ACCOUNT_DEACTIVATED("IAM-403-004", "Account is deactivated", HttpStatus.FORBIDDEN),

    // 404 - Not Found
    NOT_FOUND("IAM-404-002", "Resource not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("IAM-404-001", "User not found", HttpStatus.NOT_FOUND),

    // 405 - Method Not Allowed
    METHOD_NOT_ALLOWED("IAM-405-001", "HTTP method not supported", HttpStatus.METHOD_NOT_ALLOWED),

    // 409 - Conflict
    EMAIL_ALREADY_EXISTS("IAM-409-001", "Email is already registered", HttpStatus.CONFLICT),
    USER_ALREADY_EXISTS("IAM-409-002", "User already exists", HttpStatus.CONFLICT),
    RESOURCE_ALREADY_EXISTS("IAM-409-003", "Resource already exists", HttpStatus.CONFLICT),
    ACCOUNT_ALREADY_VERIFIED("IAM-409-004", "Account is already verified", HttpStatus.CONFLICT),

    // 415 - Unsupported Media Type
    UNSUPPORTED_MEDIA_TYPE("IAM-415-001", "Content type not supported", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    // 422 - Unprocessable Content
    BUSINESS_RULE_VIOLATION("IAM-422-001", "Business rule violation", HttpStatus.UNPROCESSABLE_CONTENT),
    INVALID_ACCOUNT_STATE("IAM-422-002", "Invalid account state", HttpStatus.UNPROCESSABLE_CONTENT),
    EMAIL_NOT_VERIFIED("IAM-422-003", "Email address is not verified", HttpStatus.UNPROCESSABLE_CONTENT),

    // 429 - Too Many Requests
    TOO_MANY_REQUESTS("IAM-429-001", "Too many requests", HttpStatus.TOO_MANY_REQUESTS),

    // 500 - Internal Server Error
    INTERNAL_ERROR("IAM-500-001", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("IAM-500-002", "A database error occurred", HttpStatus.INTERNAL_SERVER_ERROR),

    // 503 - Service Unavailable
    SERVICE_UNAVAILABLE("IAM-503-001", "Service is temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus status() {
        return status;
    }
}