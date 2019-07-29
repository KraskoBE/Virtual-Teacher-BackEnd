package com.telerikacademy.virtualteacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "enabled=1")
@Table(name = "teacher_requests")
public class TeacherRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_request_id")
    private Long id;

    @NotNull
    @OneToOne
    private User user;

    @NotNull
    @Column(name = "accepted")
    private boolean accepted = false;

    @JsonIgnore
    @NotNull
    @Column(name = "enabled")
    private boolean enabled = true;

}
