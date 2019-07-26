package com.telerikacademy.virtualteacher.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Topic;
import com.telerikacademy.virtualteacher.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class CourseResponseDTO {
    private Long id;

    private String name;

    private Topic topic;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<Lecture> lectures = new HashSet<>();
}
