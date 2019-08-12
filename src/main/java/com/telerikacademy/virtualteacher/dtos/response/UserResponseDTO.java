package com.telerikacademy.virtualteacher.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Picture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Picture picture;

    private Set<Course> createdCourses;

    private Set<Course> enrolledCourses;

    private Set<Lecture> createdLectures;

    private Set<Lecture> finishedLectures;

    private Set<Course> finishedCourses;

}
