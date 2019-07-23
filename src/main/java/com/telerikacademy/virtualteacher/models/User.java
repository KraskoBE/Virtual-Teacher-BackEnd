package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.virtualteacher.validators.EmailConstraint;
import com.telerikacademy.virtualteacher.validators.NameConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "enabled=1")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @EmailConstraint
    @Column(name = "email")
    private String email;

    @NotNull
    @NameConstraint
    @Column(name = "first_name")
    @Size(max = 15)
    private String firstName;

    @NotNull
    @NameConstraint
    @Column(name = "last_name")
    @Size(max = 15)
    private String lastName;

    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "creator")
    private Set<Lecture> createdCourses = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Course> enrolledCourses = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Lecture> createdLectures = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Lecture> finishedLectures = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Assignment> assignments = new HashSet<>();

    @NotNull
    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled = true;
}
