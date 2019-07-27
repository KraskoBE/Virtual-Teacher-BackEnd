package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.Assignment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentService {

    Assignment save(Long userId, Long lectureId, MultipartFile assignmentFile);

    Resource findByLectureIdAndUserId(Long lectureId, Long userId);
}
