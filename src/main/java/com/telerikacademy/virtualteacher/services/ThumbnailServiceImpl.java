package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Thumbnail;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.ThumbnailRepository;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import com.telerikacademy.virtualteacher.services.contracts.ThumbnailService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Optional;

@Service("ThumbnailService")
public class ThumbnailServiceImpl extends StorageServiceBase implements ThumbnailService {
    private final UserService userService;
    private final CourseService courseService;
    private final ThumbnailRepository thumbnailRepository;

    @Autowired
    @Lazy
    public ThumbnailServiceImpl(UserService userService,
                                CourseService courseService,
                                ThumbnailRepository thumbnailRepository) {
        super(
                Paths.get("./uploads/thumbnails"),
                "http://localhost:8080/api/thumbnails"
        );
        this.userService = userService;
        this.courseService = courseService;
        this.thumbnailRepository = thumbnailRepository;
    }

    @Override
    void setAllowedTypes() {
        allowedTypes.put("image/jpeg", "jpg");
    }

    @Override
    public Thumbnail save(Long authorId, Long courseId, MultipartFile thumbnailFile) {
        User author = userService.findById(authorId);
        Course course = courseService.findById(courseId);

        String fileType = allowedTypes.get(thumbnailFile.getContentType());
        String fileName = String.format("thumbnail_C%d.%s", courseId, fileType);
        String fileUrl = storeFile(thumbnailFile, courseId.toString(), fileName);

        Thumbnail newThumbnail = new Thumbnail(
                fileUrl,
                thumbnailFile.getContentType(),
                thumbnailFile.getSize(),
                fileName,
                author
        );

        Optional<Thumbnail> optThumbnail = thumbnailRepository.findByFilePath(fileUrl);
        optThumbnail.ifPresent(thumbnail -> newThumbnail.setId(thumbnail.getId()));

        return thumbnailRepository.save(newThumbnail);
    }

    @Override
    public Resource findByCourseId(Long courseId) {
        Course course = courseService.findById(courseId);

        Thumbnail thumbnail = course.getThumbnail();

        if (thumbnail == null) throw new NotFoundException("Picture not found");

        String fileName = thumbnail.getFileName();
        return loadFileByName(fileName);
    }
}
