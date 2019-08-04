package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.AuthenticationRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.AuthenticationResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.JwtProvider;
import com.telerikacademy.virtualteacher.services.contracts.AuthService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service("AuthService")
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.getEmail(),
                        authenticationRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User authenticatedUsed = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AuthenticationResponseDTO authenticationResponse = modelMapper.map(authenticatedUsed,AuthenticationResponseDTO.class);
        authenticationResponse.setToken(jwtProvider.generateToken(authentication));

        return authenticationResponse;
    }

    @Override
    public AuthenticationResponseDTO register(UserRequestDTO userRequestDTO) {
        User newUser = userService.save(userRequestDTO);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestDTO.getEmail(),
                        userRequestDTO.getPassword())
        );

        AuthenticationResponseDTO authenticationResponse = modelMapper.map(newUser, AuthenticationResponseDTO.class);
        authenticationResponse.setToken(jwtProvider.generateToken(authentication));

        return authenticationResponse;
    }
}
