package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Modifying
    @Query("update Assignment a set a.enabled = false where a.id= :assignmentId")
    void deleteById(@Param("assignmentId") Long id);

    default Optional<Assignment> findByLectureAndUser(Lecture lecture, User user) {
        return null;
    }
}