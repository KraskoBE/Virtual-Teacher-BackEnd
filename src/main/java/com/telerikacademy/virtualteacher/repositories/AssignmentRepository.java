package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Modifying
    @Query("update Assignment a set a.enabled = false where a.id= :assignmentId")
    void deleteById(@Param("assignmentId") Long id);

    Optional<Assignment> findByFilePath(String filePath);

    Optional<Assignment> findByLectureAndAuthor(Lecture lecture, User author);

    List<Assignment> findByLecture_Course_Author_IdAndGradeEquals(Long authorId, int grade);

    Optional<Assignment> findByFileName(String fileName);
}
