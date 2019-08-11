package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface VideoRepository extends JpaRepository<Video, Long> {
    @Modifying
    @Query("update Video v set v.enabled = false where v.id= :videoId")
    void deleteById(@Param("videoId") Long id);

    Optional<Video> findByLecture(Lecture lecture);

    Optional<Video> findByFilePath(String filePath);
}
