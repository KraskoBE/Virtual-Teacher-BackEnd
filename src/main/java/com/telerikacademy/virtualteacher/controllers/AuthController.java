package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.AuthenticationRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody final AuthenticationRequestDTO userAuth) {
        return ResponseEntity.ok().body(authService.login(userAuth));
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody final UserRequestDTO userRequest) {
        return ResponseEntity.ok().body(authService.register(userRequest));
    }

    @PreAuthorize("hasRole('Student')")
    @PostMapping("/validate")
    public ResponseEntity validate(@CurrentUser User currentUser) {
        return ResponseEntity.ok().body(authService.validateToken(currentUser));
    }
}
