package com.telerikacademy.virtualteacher.exceptions;

import com.telerikacademy.virtualteacher.exceptions.auth.EmailAlreadyUsedException;
import com.telerikacademy.virtualteacher.exceptions.auth.InvalidTokenException;

import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.FileNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
class GlobalExceptionHandler {

    //STORAGE_EXCEPTIONS
    @ExceptionHandler({StorageException.class})
    public void handleStorageException(
            StorageException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler({FileNotFoundException.class})
    public void handleFileNotFoundException(
            FileNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getLocalizedMessage());
    }

    //AUTH_EXCEPTIONS
    @ExceptionHandler({UserNotFoundException.class})
    public void handleUserNotFoundException(
            UserNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler({EmailAlreadyUsedException.class})
    public void handleEmailAlreadyUsedException(
            EmailAlreadyUsedException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_CONFLICT, ex.getLocalizedMessage());
    }

    @ExceptionHandler({InvalidTokenException.class})
    public void handleInvalidTokenException(
            InvalidTokenException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getLocalizedMessage());
    }
}