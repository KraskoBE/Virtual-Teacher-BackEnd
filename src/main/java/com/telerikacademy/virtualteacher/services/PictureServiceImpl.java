package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.PictureRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Optional;

@Service("PictureService")
public class PictureServiceImpl extends StorageServiceBase implements PictureService {
    private PictureRepository pictureRepository;
    private UserRepository userRepository;

    public PictureServiceImpl(PictureRepository pictureRepository, UserRepository userRepository) {
        super(
                Paths.get("./uploads/pictures"),
                "http://localhost:8080/api/pictures"
        );
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
    }

    @Override
    void setAllowedTypes() {
        allowedTypes.put("image/jpeg", "jpg");
    }

    @Override
    public Picture save(Long authorId, Long userId, MultipartFile pictureFile) {
        User author = getUser(authorId);

        String fileType = allowedTypes.get(pictureFile.getContentType());
        String fileName = String.format("picture_U%d.%s", userId, fileType);
        String fileUrl = storeFile(pictureFile, userId, fileName);

        Optional<Picture> picture = pictureRepository.findByFilePath(fileUrl);
        if (picture.isPresent()) {
            picture.get().setAuthor(author);
            return picture.get();
        }

        return pictureRepository.save(
                new Picture(
                        fileUrl,
                        pictureFile.getContentType(),
                        pictureFile.getSize(),
                        fileName,
                        author
                ));
    }

    @Override
    public Resource findByUserId(Long userId) {
        User user = getUser(userId);
        Picture picture = pictureRepository.findByAuthor(user)
                .orElseThrow(() -> new NotFoundException("Picture not found"));

        String fileName = picture.getFileName();
        return loadFileByName(fileName);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%d not found", userId)));
    }
}
