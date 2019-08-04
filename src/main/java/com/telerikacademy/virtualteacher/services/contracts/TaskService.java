package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.Task;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {

    Task save(Long authorId, Long lectureId, MultipartFile taskFile);

    Resource findByLectureId(Long lectureId);
}
