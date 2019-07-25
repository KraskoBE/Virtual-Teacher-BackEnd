package com.telerikacademy.virtualteacher.exceptions.auth;

public class EmailAlreadyUsedException extends AuthException {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }

    public EmailAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
