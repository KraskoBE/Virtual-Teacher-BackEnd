package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
import com.telerikacademy.virtualteacher.services.contracts.AssignmentService;
import com.telerikacademy.virtualteacher.services.contracts.LectureService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service("AssignmentService")
public class AssignmentServiceImpl extends StorageServiceBase implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final LectureService lectureService;
    private final UserService userService;

    @Lazy
    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 LectureService lectureService,
                                 UserService userService) {
        super(
                Paths.get("./uploads/assignments"),
                "http://localhost:8080/api/assignments"
        );
        this.assignmentRepository = assignmentRepository;
        this.userService = userService;
        this.lectureService = lectureService;
    }

    @Override
    public void setAllowedTypes() {
        allowedTypes.put("text/plain", "txt");
        allowedTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        allowedTypes.put("application/msword", "doc");
        allowedTypes.put("text/x-java-source,java", "java");
        allowedTypes.put("application/pdf","pdf");
    }

    @Override
    public Assignment findById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));
    }

    @Override
    public Assignment save(Long authorId, Long lectureId, MultipartFile assignmentFile) {
        User author = userService.findById(authorId);

        Lecture lecture = lectureService.findById(lectureId);

        String fileType = allowedTypes.get(assignmentFile.getContentType());
        String fileName = String.format("assignment_L%d_U%d.%s", lectureId, authorId, fileType);
        String fileUrl = storeFile(assignmentFile, fileName, fileName);

        Assignment newAssignment = new Assignment(
                author,
                lecture,
                fileUrl,
                assignmentFile.getContentType(),
                assignmentFile.getSize(),
                fileName
        );
        Optional<Assignment> optAssignment = assignmentRepository.findByFilePath(fileUrl);
        optAssignment.ifPresent(assignment -> newAssignment.setId(assignment.getId()));

        return assignmentRepository.save(newAssignment);
    }

    @Override
    public List<Assignment> findByCourseAuthor(Long courseAuthorId, int minimalGrade) {
        return assignmentRepository.findByLecture_Course_Author_IdAndGradeEquals(courseAuthorId, 0);
    }

    @Override
    public Resource findByFileName(String fileName) {

        Assignment assignment = assignmentRepository.findByFileName(fileName)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        return loadFileByName(fileName);
    }

    @Override
    public boolean isLastAssignment(Assignment assignment) {
        int lectureSize = assignment.getLecture().getCourse().getLectures().size();
        Long lectureInnerId = assignment.getLecture().getInnerId();

        return lectureInnerId == lectureSize;
    }
}
