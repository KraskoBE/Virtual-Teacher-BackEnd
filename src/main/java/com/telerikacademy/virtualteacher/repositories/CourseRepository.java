package com.telerikacademy.virtualteacher.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository {

    @Modifying
    @Query("update Course c set c.enabled = false where c.id= :courseId")
    void deleteById(@Param("courseId") Long id);
}
