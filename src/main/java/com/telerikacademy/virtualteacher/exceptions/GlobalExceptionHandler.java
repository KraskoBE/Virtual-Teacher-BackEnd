package com.telerikacademy.virtualteacher.exceptions;

import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.auth.EmailAlreadyUsedException;
import com.telerikacademy.virtualteacher.exceptions.auth.InvalidTokenException;

import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.FileNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
class GlobalExceptionHandler {

    //STORAGE_EXCEPTIONS
    @ExceptionHandler({FileNotFoundException.class})
    public void handleFileNotFoundException(
            FileNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler({StorageException.class})
    public void handleStorageException(
            StorageException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getLocalizedMessage());
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

    @ExceptionHandler({BadCredentialsException.class})
    public void handleBadCredentialsException(
            BadCredentialsException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getLocalizedMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public void handleAccessDeniedException(
            AccessDeniedException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getLocalizedMessage());
    }

    //GLOBAL_EXCEPTIONS
    @ExceptionHandler({NotFoundException.class})
    public void handleNotFoundException(
            NotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    public void handleBadRequestException(
            BadRequestException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public void handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler({AlreadyExistsException.class})
    public void handleAlreadyExistsException(
            AlreadyExistsException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_CONFLICT, ex.getLocalizedMessage());
    }

    /*@ExceptionHandler({RuntimeException.class})
    public void handleRuntimeException(
            RuntimeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getLocalizedMessage());
    }*/
}