package com.telerikacademy.virtualteacher.exceptions;

import com.telerikacademy.virtualteacher.dtos.response.ErrorResponse;
import com.telerikacademy.virtualteacher.exceptions.auth.InvalidTokenException;
import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.FileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
class GlobalExceptionHandler {

    /*@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Invalid token")
    @ExceptionHandler(InvalidTokenException.class)
    public void handleInvalidToken() {
    }*/

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFound() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "File not found")
    @ExceptionHandler(FileNotFoundException.class)
    public void handleFileNotFound() {
    }


    @ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity<ErrorResponse> handleUserNotFoundException
            (InvalidTokenException ex, WebRequest request)
    {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("INCORRECT_REQUEST", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}