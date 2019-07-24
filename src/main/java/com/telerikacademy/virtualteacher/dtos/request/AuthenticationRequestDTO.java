package com.telerikacademy.virtualteacher.dtos.request;

import com.telerikacademy.virtualteacher.validators.EmailConstraint;
import com.telerikacademy.virtualteacher.validators.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequestDTO {
    @NotNull
    @EmailConstraint
    private String email;

    @NotNull
    @PasswordConstraint
    private String password;
}
