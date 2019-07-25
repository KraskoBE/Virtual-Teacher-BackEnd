package com.telerikacademy.virtualteacher.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository {
    @Modifying
    @Query("update Assignment a set a.enabled = false where a.id= :assignmentId")
    void deleteById(@Param("assignmentId") Long id);
}
