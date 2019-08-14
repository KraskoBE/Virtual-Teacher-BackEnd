package com.telerikacademy.virtualteacher.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseRatingResponseDTO {
    private Long user;

    private Long course;

    private int rating;
}
