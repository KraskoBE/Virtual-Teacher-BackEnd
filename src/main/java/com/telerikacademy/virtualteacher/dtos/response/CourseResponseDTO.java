package com.telerikacademy.virtualteacher.dtos.response;

import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Topic;
import com.telerikacademy.virtualteacher.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseResponseDTO {
    private Long id;

    private String name;

    private Topic topic;

    private String description;

    private boolean submitted;

    private Set<Lecture> lectures;

    private Set<User> users;

    private double averageRating;

    private int totalVotes;
}
