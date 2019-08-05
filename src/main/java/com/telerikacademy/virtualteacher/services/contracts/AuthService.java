package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.dtos.request.AuthenticationRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.AuthenticationResponseDTO;
import com.telerikacademy.virtualteacher.models.User;

public interface AuthService {

    AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO);

    AuthenticationResponseDTO register(UserRequestDTO userRequestDTO);

    AuthenticationResponseDTO validateToken(User user);
}
