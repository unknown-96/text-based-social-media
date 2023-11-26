package com.example.socialmedia.exception;

public class TooLongTextException extends Exception {
    public TooLongTextException() {
        super("The text is over a 1000 characters long");
    }
}
