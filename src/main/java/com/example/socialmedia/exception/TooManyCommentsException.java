package com.example.socialmedia.exception;

public class TooManyCommentsException extends Exception {
    public TooManyCommentsException() {
        super("A user cannot post more than 10 times per post");
    }
}
