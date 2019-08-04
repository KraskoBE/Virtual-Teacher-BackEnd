package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.dtos.request.AuthenticationRequestDTO;
import com.telerikacademy.virtualteacher.models.User;

public interface AuthService {

    User login(AuthenticationRequestDTO authenticationRequestDTO);
}
