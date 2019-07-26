package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.FileNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Task;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.repositories.TaskRepository;
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
@Service("TaskService")
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private LectureRepository lectureRepository;

    private final String rootUrl = "http://localhost:8080/api/tasks";
    private final Path rootLocation = Paths.get("./uploads/tasks");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public Task save(Long userId, Long lectureId, MultipartFile taskFile) {
        Lecture lecture = getLecture(lectureId);
        User user = getUser(userId);

        if (lecture.getAuthor() != user) {
            throw new BadRequestException("User is not creator of this course");
        }

        checkFileType(taskFile);

        String fileExtension = "txt";//Objects.requireNonNull(taskFile.getContentType()).substring(taskFile.getContentType().indexOf('/'+1));
        String fileName = String.format("Task_lecture_%d.%s",lectureId, fileExtension);
        System.out.println(fileName);
        String taskPath = storeFile(taskFile, lectureId, fileName);

        return taskRepository.save(
            new Task(user,
                    lecture,
                    taskPath,
                    taskFile.getContentType(),
                    taskFile.getSize(),
                    fileName)
        );
    }

    @Override
    public Resource findByLectureId(Long lectureId) {
        Lecture lecture = getLecture(lectureId);

        Task task = taskRepository.findByLecture(lecture)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        try {
            Path file = rootLocation.resolve(task.getFileName());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: ");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: ",e);
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
