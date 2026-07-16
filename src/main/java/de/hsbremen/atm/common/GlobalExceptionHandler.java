package de.hsbremen.atm.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    ResponseEntity<ApiError> handleDomainException(DomainException exception) {
        return ResponseEntity
                .status(exception.status())
                .body(new ApiError(exception.code(), exception.getMessage()));
    }
}

