package com.telerikacademy.virtualteacher.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerikacademy.virtualteacher.validators.EmailConstraint;
import com.telerikacademy.virtualteacher.validators.NameConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserResponseDTO {
    @NotNull
    @EmailConstraint
    private String email;

    @NotNull
    @NameConstraint
    private String firstName;

    @NotNull
    @NameConstraint
    private String lastName;

    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
