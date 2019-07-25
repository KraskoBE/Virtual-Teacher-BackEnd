package com.telerikacademy.virtualteacher.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository {
    @Modifying
    @Query("update Task t set t.enabled = false where t.id= :taskId")
    void deleteById(@Param("taskId") Long id);
}
