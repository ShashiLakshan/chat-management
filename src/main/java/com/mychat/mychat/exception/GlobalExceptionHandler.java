package com.mychat.mychat.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> onValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("Validation error");
        return build(req, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", msg);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> onNotFound(NotFoundException ex, HttpServletRequest req) {
        return build(req, HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> onForbidden(ForbiddenException ex, HttpServletRequest req) {
        return build(req, HttpStatus.FORBIDDEN, "FORBIDDEN", "You cannot access this resource");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> onOthers(Exception ex, HttpServletRequest req) {
        return build(req, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error");
    }

    private ResponseEntity<Map<String, Object>> build(HttpServletRequest req, HttpStatus status, String code, String msg) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "path", req.getRequestURI(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "code", code,
                "message", msg,
                "traceId", UUID.randomUUID().toString()
        );
        return ResponseEntity.status(status).body(body);
    }

}
