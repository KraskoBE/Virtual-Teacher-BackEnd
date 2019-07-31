package com.telerikacademy.virtualteacher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CourseRatingId.class)
@Table(name = "course_rating")
public class CourseRating {

    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Course course;

    private int rating;
}
