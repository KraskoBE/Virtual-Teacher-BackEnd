package com.telerikacademy.virtualteacher.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LectureRequestDTO {

    @NotBlank
    @Size(min = 3, max = 25)
    private String name;

    @Size(min = 10, max = 3000)
    private String description;
}
