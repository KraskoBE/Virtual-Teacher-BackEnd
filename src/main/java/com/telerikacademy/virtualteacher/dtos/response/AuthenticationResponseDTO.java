package com.telerikacademy.virtualteacher.dtos.response;

import com.telerikacademy.virtualteacher.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponseDTO {

    private Long id;

    private String email;

    private String firstName;

    private String token;

    private Collection<Role> roles;
}
