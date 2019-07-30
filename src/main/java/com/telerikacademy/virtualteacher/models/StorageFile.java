package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnore
    Long fileSize;

    @NotNull
    @Column(name = "file_name")
    @JsonIgnore
    String fileName;

    @ManyToOne
    @JoinColumn(name = "author")
    @JsonIgnore
    User author;

}
