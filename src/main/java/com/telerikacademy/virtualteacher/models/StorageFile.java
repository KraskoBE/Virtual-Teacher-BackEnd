package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Getter
@Setter
abstract class StorageFile {

    @NotNull
    @Column(name = "file_path")
    String filePath;

    @NotNull
    @Column(name = "file_type")
    String fileType;

    @NotNull
    @Column(name = "file_size")
    Long fileSize;

    @NotNull
    @Column(name = "file_name")
    String fileName;

    @ManyToOne
    @JoinColumn(name = "author")
    @JsonIgnore
    User author;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    @JsonIgnore
    Lecture lecture;
}
