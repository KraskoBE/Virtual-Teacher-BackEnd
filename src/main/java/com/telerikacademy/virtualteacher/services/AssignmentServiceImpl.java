package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service("AssignmentService")
public class AssignmentServiceImpl extends StorageServiceBase implements AssignmentService {

    private AssignmentRepository assignmentRepository;
    private UserRepository userRepository;
    private LectureRepository lectureRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 UserRepository userRepository,
                                 LectureRepository lectureRepository) {
        super(
                Paths.get("./uploads/assignments"),
                "http://localhost:8080/api/assignments"
        );
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
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
        User author = getUser(authorId);
        Lecture lecture = getLecture(lectureId);

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
        Lecture lecture = getLecture(lectureId);
        User user = getUser(userId);

        Assignment assignment = assignmentRepository.findByLectureAndUser(lecture, user)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        String fileName = assignment.getFileName();
        return loadFileByName(fileName);
    }

    //EOF interface methods

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException(String.format("Lecture with id:%d not found", lectureId)));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%d not found", userId)));
    }
}
