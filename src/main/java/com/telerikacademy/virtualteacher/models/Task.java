package com.telerikacademy.virtualteacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "enabled=1")
@Table(name = "tasks")
public class Task extends StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lecture_id", nullable = false)
    @JsonIgnore
    Lecture lecture;

    @NotNull
    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled = true;

    public Task(User author, Lecture lecture, String filePath, String fileType, Long fileSize, String fileName) {
        super(filePath, fileType, fileSize, fileName, author);
        this.lecture = lecture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
