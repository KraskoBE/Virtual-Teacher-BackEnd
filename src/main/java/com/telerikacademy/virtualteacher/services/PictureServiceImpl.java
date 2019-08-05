package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.PictureRepository;
import com.telerikacademy.virtualteacher.services.contracts.PictureService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Optional;

@Service("PictureService")
public class PictureServiceImpl extends StorageServiceBase implements PictureService {
    private final PictureRepository pictureRepository;
    private final UserService userService;

    @Autowired
    @Lazy
    public PictureServiceImpl(PictureRepository pictureRepository,
                              UserService userService) {
        super(
                Paths.get("./uploads/pictures"),
                "http://localhost:8080/api/pictures"
        );
        this.pictureRepository = pictureRepository;
        this.userService = userService;
    }

    @Override
    void setAllowedTypes() {
        allowedTypes.put("image/jpeg", "jpg");
    }

    @Override
    public Picture save(Long authorId, Long userId, MultipartFile pictureFile) {
        User author = userService.findById(authorId);

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
        User user = userService.findById(userId);
        Picture picture = user.getPicture();
        if (picture == null) throw new NotFoundException("Picture not found");

        String fileName = picture.getFileName();
        return loadFileByName(fileName);
    }
}
