package com.telerikacademy.virtualteacher.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureRepository {

    @Modifying
    @Query("update Lecture l set l.enabled = false where l.id= :lectureId")
    void deleteById(@Param("lectureId") Long id);
}
