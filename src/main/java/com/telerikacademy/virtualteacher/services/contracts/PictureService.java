package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.Picture;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    Picture save(Long authorId, Long userId, MultipartFile pictureFile);

    Resource findByUserId(Long userId);
}
