package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.models.Video;
import com.telerikacademy.virtualteacher.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service("VideoService")
public class VideoServiceImpl extends StorageServiceBase implements VideoService {

    private final VideoRepository videoRepository;
    private final UserService userService;
    private final LectureService lectureService;

    @Autowired
    @Lazy
    public VideoServiceImpl(VideoRepository videoRepository,
                            UserService userService,
                            LectureService lectureService) {
        super(
                Paths.get("./uploads/videos"),
                "http://localhost:8080/api/videos"
        );

        this.videoRepository = videoRepository;
        this.userService = userService;
        this.lectureService = lectureService;
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
        User author = userService.findById(authorId);
        Lecture lecture = lectureService.findById(lectureId);

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
        Lecture lecture = lectureService.findById(lectureId);
        Video video = videoRepository.findByLecture(lecture)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        String fileName = video.getFileName();

        return loadFileByName(fileName);
    }
}
