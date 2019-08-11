package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 25)
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    @OneToOne
    @JoinColumn(name = "topic")
    private Topic topic;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "submitted")
    private boolean submitted = false;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne
    @JoinColumn(name = "thumbnail")
    private Thumbnail thumbnail;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<Lecture> lectures = new TreeSet<>(Comparator.comparing(Lecture::getInnerId));

    @ManyToMany
    @JoinTable(name = "user_course",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "graduated_users",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> graduatedUsers = new HashSet<>();

    @NotNull
    @Column(name = "average_rating")
    private double averageRating = 0.0;

    @NotNull
    @Column(name = "total_votes")
    private int totalVotes = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<CourseRating> courseRatings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
