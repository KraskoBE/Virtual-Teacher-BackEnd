package com.telerikacademy.virtualteacher.dtos.request;

import com.telerikacademy.virtualteacher.validators.EmailConstraint;
import com.telerikacademy.virtualteacher.validators.NameConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequestDTO {
    @NotNull
    @EmailConstraint
    private String email;

    @NotNull
    @NameConstraint
    private String firstName;

    @NotNull
    @NameConstraint
    private String lastName;
}
