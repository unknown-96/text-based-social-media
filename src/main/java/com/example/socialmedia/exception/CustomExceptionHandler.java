package com.example.socialmedia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(AccountExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAccountExists(AccountExistsException accountExistsException) {
        return new ResponseEntity<>(accountExistsException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TooLongTextException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleTooLongText(TooLongTextException tooLongTextException) {
        return new ResponseEntity<>(tooLongTextException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TooManyCommentsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleTooLongText(TooManyCommentsException tooManyCommentsException) {
        return new ResponseEntity<>(tooManyCommentsException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
