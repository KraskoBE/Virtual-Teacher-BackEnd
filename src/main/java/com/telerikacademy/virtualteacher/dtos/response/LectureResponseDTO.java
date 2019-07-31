package com.telerikacademy.virtualteacher.dtos.response;

import com.telerikacademy.virtualteacher.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LectureResponseDTO {
    private Long id;

    private Long innerId;

    private Course course;

    private String name;

    private String description;

    private Task task;

    private User author;

    private Video video;

    private Set<User> users;

    private Set<Assignment> assignments;
}
