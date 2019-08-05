package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    @Modifying
    @Query("update Thumbnail t set t.enabled = false where t.id= :thumbnailId")
    void deleteById(@Param("thumbnailId") Long id);


    Optional<Thumbnail> findByFilePath(String filePath);
}
