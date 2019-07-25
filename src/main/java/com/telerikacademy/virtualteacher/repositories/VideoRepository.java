package com.telerikacademy.virtualteacher.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRepository {
    @Modifying
    @Query("update Video v set v.enabled = false where v.id= :videoId")
    void deleteById(@Param("videoId") Long id);
}
