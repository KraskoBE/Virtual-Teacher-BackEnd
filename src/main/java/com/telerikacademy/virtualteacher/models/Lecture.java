package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "enabled=1")
@Table(name = "lectures")
class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    @Column(name= "inner_id")
    private Long innerId;

    @ManyToOne
    @JoinColumn(name="course", nullable = false)
    private Course course;

    @NotBlank
    @Size(min = 3, max = 25)
    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "task_path")
    private String taskPath;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creator")
    private User creator;

    @URL
    @Column(name = "video_url")
    private String videoUrl;

    @ManyToMany
    @JoinTable(name = "user_lecture",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "lecture_id")})
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "lecture")
    private Set<Assignment> assignments = new HashSet<>();

    @NotNull
    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled = true;
}
