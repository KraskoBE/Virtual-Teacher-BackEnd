package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.Assignment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentService {

    Assignment findById(Long id);

    Assignment save(Long authorId, Long lectureId, MultipartFile assignmentFile);


    Resource findByLectureIdAndUserId(Long lectureId, Long userId);

    boolean isLastAssignment(Assignment assignment);
}
