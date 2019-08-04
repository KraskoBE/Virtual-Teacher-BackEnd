package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Modifying
    @Query("update Lecture l set l.enabled = false where l.id= :lectureId")
    void deleteById(@Param("lectureId") Long id);

    Optional<Lecture> findByNameIgnoreCase(String name);
    List<Lecture> findByCourse_Id(Long courseId);
}
