package com.beside.ties.global.common.exception;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.beside.ties.global.common.exception.custom.InvalidSocialTokenException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidSocialTokenException.class)
    ResponseEntity<String> handleNoSuchElementException(InvalidSocialTokenException e) {
        log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<String> handleIllegalArgumentException(DateTimeParseException e) {
        log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(SignatureVerificationException.class)
    ResponseEntity<String> handleSignatureVerificationException(SignatureVerificationException e) {
        log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    ResponseEntity<String> handleInvalidFormatException(InvalidFormatException e) {
        log.error(String.valueOf(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }






}
