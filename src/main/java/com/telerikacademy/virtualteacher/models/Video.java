package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "enabled=1")
@Table(name = "videos")
public class Video extends StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lecture_id", nullable = false)
    @JsonIgnore
    Lecture lecture;

    @NotNull
    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled = true;

    public Video(User author, Lecture lecture, String filePath, String fileType, Long fileSize, String fileName) {
        super(filePath, fileType, fileSize, fileName, author);
        this.lecture = lecture;
    }
}
