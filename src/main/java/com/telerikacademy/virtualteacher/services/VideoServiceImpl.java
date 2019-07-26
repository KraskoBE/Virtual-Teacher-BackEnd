package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.models.Video;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import com.telerikacademy.virtualteacher.repositories.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service("VideoService")
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    private final String rootUrl = "http://localhost:8080/api/videos";
    private final Path rootLocation = Paths.get("./uploads/videos");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public Video save(Long userId, Long lectureId, MultipartFile videoFile) {
        Lecture lecture = getLecture(lectureId);
        User user = getUser(userId);

        if (lecture.getCreator() != user)
            throw new BadRequestException("User is not creator of the course");

        checkFileType(videoFile);

        String videoPath = storeFile(videoFile, lectureId);

        return videoRepository.save(new Video(user, lecture, videoPath, videoFile.getContentType(), videoFile.getSize()));
    }

    @Override
    public Optional<Video> findByLectureId(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException("Lecture not found"));
        return videoRepository.findByLecture(lecture);
    }

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException(String.format("Lecture with id:%d not found", lectureId)));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%d not found", userId)));
    }

    private String storeFile(MultipartFile file, Long lectureId) {
        String filename = String.format("l%d", lectureId);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file ");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }
        return String.format("%s/%d", this.rootUrl, lectureId);
    }

    private void checkFileType(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).equals("video/mp4"))
            throw new BadRequestException("File is not valid format");
    }
}
