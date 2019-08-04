package com.telerikacademy.virtualteacher.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponseDTO {

    private Long id;

    private String email;

    private String firstName;

    private String token;
}
