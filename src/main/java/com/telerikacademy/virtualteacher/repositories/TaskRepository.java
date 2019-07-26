package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    @Modifying
    @Query("update Task t set t.enabled = false where t.id= :taskId")
    void deleteById(@Param("taskId") Long id);

    Optional<Task> findByLecture(Lecture lecture);
}
