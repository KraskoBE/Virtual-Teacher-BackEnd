package com.telerikacademy.virtualteacher.exceptions.auth;

public class AccessDeniedException extends AuthException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
