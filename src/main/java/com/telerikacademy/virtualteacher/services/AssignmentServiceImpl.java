package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.FileNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@AllArgsConstructor
@Service("AssignmentService")
public class AssignmentServiceImpl implements AssignmentService {

    private AssignmentRepository assignmentRepository;
    private UserRepository userRepository;
    private LectureRepository lectureRepository;

    private final String rootUrl = "htpp://localhost:8080/api/assignments";
    private final Path rootLocation = Paths.get("./uploads/assignments");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location ", e);
        }
    }

    @Override
    public Assignment save(Long userId, Long lectureId, MultipartFile assignmentFile) {
        Lecture lecture = getLecture(lectureId);
        User user = getUser(userId);

        if ( !lecture.getUsers().contains(user) ) {
            throw new BadRequestException("User is not enrolled in this course");
        }

        checkFileType(assignmentFile);

        String fileExtension = ".txt";
        String fileName = String.format("Assignment_lecture_%d_user_%d.%s", fileExtension);
        System.out.println(fileName);
        String assignmentPath = storeFile(assignmentFile, lectureId, fileName);

        return assignmentRepository.save(
            new Assignment(
                    user,
                    lecture,
                    assignmentPath,
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

        try {
            Path file = rootLocation.resolve(assignment.getFileName());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: ");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: ", e);
        }
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

    private String storeFile(MultipartFile file, Long lectureId, String fileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }
        return String.format("%s/%d", this.rootUrl, lectureId);
    }

    private void checkFileType(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).equals("text/plain")) {
            throw new BadRequestException("File is not valid format");
        }
    }

}
