package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
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

    @NotNull
    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled = true;

    public Video(User author, Lecture lecture, String filePath, String fileType, Long fileSize, String fileName) {
        this.author = author;
        this.lecture = lecture;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileName = fileName;
    }
}
