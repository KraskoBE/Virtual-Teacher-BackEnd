package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.AuthenticationRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.security.JwtProvider;
import com.telerikacademy.virtualteacher.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider provider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtProvider provider,
                          UserService userService,
                          ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.provider = provider;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody final AuthenticationRequestDTO userAuth,
                                final HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuth.getEmail(),
                        userAuth.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = provider.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody final UserRequestDTO user,
                                   final HttpServletResponse response) {
        return userService.save(user)
                .map(record ->
                        ResponseEntity.ok().body(modelMapper.map(record, UserResponseDTO.class)))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use"));
    }
}
