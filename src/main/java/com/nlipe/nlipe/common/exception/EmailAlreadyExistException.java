package com.nlipe.nlipe.common.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("Email already exist");
    }
}