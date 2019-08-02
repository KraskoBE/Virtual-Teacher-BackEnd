package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

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
    }

    @Override
    public Assignment save(Long authorId, Long lectureId, MultipartFile assignmentFile) {
        User author = userService.findById(authorId);

        Lecture lecture = lectureService.findById(lectureId);

        String fileType = allowedTypes.get(assignmentFile.getContentType());
        String fileName = String.format("assignment_L%d_U%d.%s", lectureId, authorId, fileType);
        String fileUrl = storeFile(assignmentFile, lectureId, fileName);

        return assignmentRepository.save(
                new Assignment(
                        author,
                        lecture,
                        fileUrl,
                        assignmentFile.getContentType(),
                        assignmentFile.getSize(),
                        fileName
                )
        );
    }

    @Override
    public Resource findByLectureIdAndUserId(Long lectureId, Long userId) {
        Lecture lecture = lectureService.findById(lectureId);

        User user = userService.findById(userId);

        Assignment assignment = getAssignment(lecture, user);

        String fileName = assignment.getFileName();
        return loadFileByName(fileName);
    }

    @Override
    public boolean isLastAssignment(Assignment assignment) {
        int lectureSize = assignment.getLecture().getCourse().getLectures().size();
        Long lectureInnerId = assignment.getLecture().getInnerId();

        return lectureInnerId == lectureSize;
    }

    private Assignment getAssignment(Lecture lecture, User user) {
        return assignmentRepository.findByLectureAndUser(lecture, user)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));
    }
}
