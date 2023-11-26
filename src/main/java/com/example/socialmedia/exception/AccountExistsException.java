package com.example.socialmedia.exception;

public class AccountExistsException extends Exception {
    public AccountExistsException() {
        super("The account already exists");
    }
}
