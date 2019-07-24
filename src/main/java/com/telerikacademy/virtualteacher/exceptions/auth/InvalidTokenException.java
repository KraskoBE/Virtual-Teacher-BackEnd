package com.telerikacademy.virtualteacher.exceptions.auth;


public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
