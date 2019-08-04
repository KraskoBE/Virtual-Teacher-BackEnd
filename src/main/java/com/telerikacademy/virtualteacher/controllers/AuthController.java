package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.AuthenticationRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.AuthenticationResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.JwtProvider;
import com.telerikacademy.virtualteacher.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody final AuthenticationRequestDTO userAuth) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuth.getEmail(),
                        userAuth.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("email", userAuth.getEmail());
        responseMap.put("token", jwtProvider.generateToken(authentication));

        return ResponseEntity.ok().body(responseMap);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody final UserRequestDTO userRequestDTO) {
        User newUser = userService.save(userRequestDTO);
        AuthenticationResponseDTO userResponse = modelMapper.map(newUser, AuthenticationResponseDTO.class);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestDTO.getEmail(),
                        userRequestDTO.getPassword())
        );

        String token = jwtProvider.generateToken(authentication);

        userResponse.setToken(token);

        return ResponseEntity.ok().body(userResponse);
    }
}
