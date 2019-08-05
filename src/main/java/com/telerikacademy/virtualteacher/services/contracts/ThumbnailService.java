package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.Thumbnail;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ThumbnailService {

    Thumbnail save(Long authorId, Long courseId, MultipartFile thumbnailFile);

    Resource findByCourseId(Long courseId);
}
