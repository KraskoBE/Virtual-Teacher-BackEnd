package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.Video;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    Video save(Long authorId, Long lectureId, MultipartFile videoFile);

    Resource findByLectureId(Long lectureId);

}
