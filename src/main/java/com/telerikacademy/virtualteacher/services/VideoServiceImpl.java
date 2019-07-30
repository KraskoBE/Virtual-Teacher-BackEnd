package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.models.Video;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import com.telerikacademy.virtualteacher.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service("VideoService")
public class VideoServiceImpl extends StorageServiceBase implements VideoService {

    private VideoRepository videoRepository;
    private UserRepository userRepository;
    private LectureRepository lectureRepository;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository,
                            UserRepository userRepository,
                            LectureRepository lectureRepository) {
        super(
                Paths.get("./uploads/videos"),
                "http://localhost:8080/api/videos"
        );

        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
    }

    @Override
    public void setAllowedTypes() {
        allowedTypes.put("video/mp4", "mp4");
        allowedTypes.put("application/mp4", "mp4");
        allowedTypes.put("video/x-msvideo", "avi");
    }

    //Beginning of interface methods
    @Override
    public Video save(Long authorId, Long lectureId, MultipartFile videoFile) {
        User author = getUser(authorId);
        Lecture lecture = getLecture(lectureId);

        String fileType = allowedTypes.get(videoFile.getContentType());
        String fileName = String.format("video_L%d.%s", lectureId, fileType);
        String fileUrl = storeFile(videoFile, lectureId, fileName);

        return videoRepository.save(
                new Video(
                        author,
                        lecture,
                        fileUrl,
                        videoFile.getContentType(),
                        videoFile.getSize(),
                        fileName
                )
        );
    }

    @Override
    public Resource findByLectureId(Long lectureId) {
        Lecture lecture = getLecture(lectureId);
        Video video = videoRepository.findByLecture(lecture)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        String fileName = video.getFileName();

        return loadFileByName(fileName);
    }
    //End of interface methods

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException(String.format("Lecture with id:%d not found", lectureId)));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%d not found", userId)));
    }
}
