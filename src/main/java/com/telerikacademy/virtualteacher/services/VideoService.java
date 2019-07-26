package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface VideoService {

    Video save(Long userId, Long lectureId, MultipartFile videoFile);

    Optional<Video> findByLectureId(Long lectureId);

}
