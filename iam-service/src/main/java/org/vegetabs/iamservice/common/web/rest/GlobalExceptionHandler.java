package org.vegetabs.iamservice.common.web.rest;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.vegetabs.iamservice.common.error.DomainException;
import org.vegetabs.iamservice.common.error.ErrorCode;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(DomainException ex) {
        ErrorCode code = ex.errorCode();
        log.warn("Domain error {}: {}", code.code(), ex.getMessage());
        return build(code, ApiError.of(code, ex.getMessage(), traceId()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBodyValidation(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> errors = ex.getBindingResult().getAllErrors().stream()
            .map(this::toFieldError)
            .toList();
        return validationFailed(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiError> handleMethodValidation(HandlerMethodValidationException ex) {
        List<ApiError.FieldError> errors = ex.getParameterValidationResults().stream()
            .flatMap(result -> {
                String parameter = result.getMethodParameter().getParameterName();
                return result.getResolvableErrors().stream().map(error ->
                    new ApiError.FieldError(
                        error instanceof FieldError fe ? fe.getField() : parameter,
                        lastCode(error),
                        error.getDefaultMessage()));
            })
            .toList();
        return validationFailed(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError.FieldError> errors = ex.getConstraintViolations().stream()
            .map(v -> {
                String path = v.getPropertyPath().toString();
                return new ApiError.FieldError(
                    path.substring(path.lastIndexOf('.') + 1),
                    v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                    v.getMessage());
            })
            .toList();
        return validationFailed(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex) {
        log.warn("Unreadable request body: {}", ex.getMessage());
        ErrorCode code = ErrorCode.BAD_REQUEST;
        return build(code, ApiError.of(code, traceId()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valid value";
        ErrorCode code = ErrorCode.BAD_REQUEST;
        List<ApiError.FieldError> errors = List.of(
            new ApiError.FieldError(ex.getName(), "TypeMismatch", "must be a valid " + expected));
        return build(code, ApiError.of(code, errors, traceId()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException ex) {
        ErrorCode code = ErrorCode.BAD_REQUEST;
        List<ApiError.FieldError> errors = List.of(
            new ApiError.FieldError(ex.getParameterName(), "Required", "is required"));
        return build(code, ApiError.of(code, errors, traceId()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException ex) {
        ErrorCode code = ErrorCode.NOT_FOUND;
        return build(code, ApiError.of(code, traceId()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ErrorCode code = ErrorCode.METHOD_NOT_ALLOWED;
        return build(code, ApiError.of(code, traceId()), ex.getHeaders());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        ErrorCode code = ErrorCode.UNSUPPORTED_MEDIA_TYPE;
        return build(code, ApiError.of(code, traceId()), ex.getHeaders());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        // Any other Spring MVC exception that already carries a status
        if (ex instanceof ErrorResponse springError) {
            HttpStatusCode status = springError.getStatusCode();
            ErrorCode code = toErrorCode(status);
            if (status.is5xxServerError()) {
                log.error("Server error", ex);
            }
            return ResponseEntity.status(status)
                .headers(springError.getHeaders())
                .body(ApiError.of(code, traceId()));
        }

        log.error("Unexpected error", ex);
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        return build(code, ApiError.of(code, traceId()));
    }

    private ResponseEntity<ApiError> validationFailed(List<ApiError.FieldError> errors) {
        ErrorCode code = ErrorCode.VALIDATION_FAILED;
        return build(code, ApiError.of(code, errors, traceId()));
    }

    private ApiError.FieldError toFieldError(ObjectError error) {
        if (error instanceof FieldError fe) {
            return new ApiError.FieldError(fe.getField(), fe.getCode(), fe.getDefaultMessage());
        }
        // class-level constraint, e.g. @PasswordsMatch on the whole DTO
        return new ApiError.FieldError(error.getObjectName(), error.getCode(), error.getDefaultMessage());
    }

    private static String lastCode(MessageSourceResolvable resolvable) {
        String[] codes = resolvable.getCodes();
        return (codes == null || codes.length == 0) ? null : codes[codes.length - 1];
    }

    private static ErrorCode toErrorCode(HttpStatusCode status) {
        return switch (status.value()) {
            case 404 -> ErrorCode.NOT_FOUND;
            case 405 -> ErrorCode.METHOD_NOT_ALLOWED;
            case 415 -> ErrorCode.UNSUPPORTED_MEDIA_TYPE;
            default -> status.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;
        };
    }

    private ResponseEntity<ApiError> build(ErrorCode code, ApiError body) {
        return build(code, body, HttpHeaders.EMPTY);
    }

    private ResponseEntity<ApiError> build(ErrorCode code, ApiError body, HttpHeaders headers) {
        return ResponseEntity.status(code.status())
            .headers(headers)
            .body(body);
    }

    private String traceId() {
        return MDC.get("traceId");
    }
}