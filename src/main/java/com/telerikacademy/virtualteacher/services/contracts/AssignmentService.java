package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.Assignment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    Assignment findById(Long id);

    Assignment save(Long authorId, Long lectureId, MultipartFile assignmentFile);

    List<Assignment> findByCourseAuthor(Long courseAuthorId, int minimalGrade);

    Resource findByFileName(String fileName);

    boolean isLastAssignment(Assignment assignment);
}
