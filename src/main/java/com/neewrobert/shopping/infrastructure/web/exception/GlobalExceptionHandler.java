package com.neewrobert.shopping.infrastructure.web.exception;

import com.neewrobert.shopping.domain.exception.UserAlreadyExistsException;
import com.neewrobert.shopping.infrastructure.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        var response = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        var response = new ErrorResponse("Validation failed", HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
