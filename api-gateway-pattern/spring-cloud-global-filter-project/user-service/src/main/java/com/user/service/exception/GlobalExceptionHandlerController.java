package com.user.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        LOGGER.info("@@@ handleResourceNotFoundException errorMessage : {} ", ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.NOT_FOUND.name(), "statusCode", HttpStatus.NOT_FOUND.value(), "statusMessage", HttpStatus.NOT_FOUND.getReasonPhrase(), "errorMessage", ex.getMessage(), "success", Boolean.TRUE), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflictException(ResourceConflictException ex) {
        LOGGER.info("@@@ handleResourceConflictException errorMessage : {} ", ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.CONFLICT.name(), "statusCode", HttpStatus.CONFLICT.value(), "statusMessage", HttpStatus.CONFLICT.getReasonPhrase(), "errorMessage", ex.getMessage(), "success", Boolean.TRUE), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
        LOGGER.info("@@@ handleBadCredentialsException errorMessage : {} ", ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.FORBIDDEN.name(), "statusCode", HttpStatus.FORBIDDEN.value(), "statusMessage", HttpStatus.FORBIDDEN.getReasonPhrase(), "errorMessage", ex.getMessage(), "success", Boolean.FALSE), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.info("@@@ handleMethodArgumentNotValidException errorMessage : {} ", ex.getMessage());
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.BAD_REQUEST.name(), "statusCode", HttpStatus.BAD_REQUEST.value(), "statusMessage", HttpStatus.BAD_REQUEST.getReasonPhrase(), "errorMessage", errors, "success", Boolean.FALSE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        LOGGER.info("@@@ handleAuthorizationDeniedException errorMessage : {} ", ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.UNAUTHORIZED.name(), "statusCode", HttpStatus.UNAUTHORIZED.value(), "statusMessage", HttpStatus.UNAUTHORIZED.getReasonPhrase(), "errorMessage", ex.getMessage(), "success", Boolean.FALSE), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        LOGGER.info("@@@ handleGlobalException errorMessage : {} ", ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.INTERNAL_SERVER_ERROR.name(), "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value(), "statusMessage", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "errorMessage", ex.getMessage(), "success", Boolean.FALSE), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
