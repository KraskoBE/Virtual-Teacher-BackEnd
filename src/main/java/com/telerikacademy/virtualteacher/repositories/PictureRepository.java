package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Modifying
    @Query("update Picture p set p.enabled = false where p.id= :pictureId")
    void deleteById(@Param("pictureId") Long id);

    Optional<Picture> findByAuthor(User author);

    Optional<Picture> findByFilePath(String filePath);
}
