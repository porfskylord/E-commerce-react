package com.ec.ecommerce_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorResponse = Map.of(
                "timestamp", Instant.now(),
                "status", ex.getStatusCode().value(),
                "error", ex.getStatusCode().toString(),
                "message", ex.getReason() != null ? ex.getReason() : "Request failed"
        );
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Bad Request",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Internal Server Error",
                        "message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred"
                ));
    }
}
