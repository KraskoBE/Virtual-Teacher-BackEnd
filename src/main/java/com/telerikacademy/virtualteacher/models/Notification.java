package com.telerikacademy.virtualteacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "enabled=1")
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "notification_id" )
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;


    @NotBlank
    @Column( name = "message" )
    private String message;

    @NotNull
    @Column( name = "seen")
    private boolean seen = false;

    @NotNull
    @JsonIgnore
    @Column(name = "enabled")
    private boolean enabled = true;

    public Notification(User receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

}
