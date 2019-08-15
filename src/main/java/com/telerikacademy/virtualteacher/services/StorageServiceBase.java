package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.storage.FileNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public abstract class StorageServiceBase {

    private final Path rootLocation;
    private final String rootUrl;
    Map<String, String> allowedTypes;

    public StorageServiceBase(Path rootLocation, String rootUrl) {
        this.rootLocation = rootLocation;
        this.rootUrl = rootUrl;
        allowedTypes = new HashMap<>();
        setAllowedTypes();
    }

    @PostConstruct
    public void initDirectory() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    final String storeFile(MultipartFile file, String fileId, String fileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file ");
            }

            checkFileType(file);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }
        return String.format("%s/%s", this.rootUrl, fileId);
    }

    private void checkFileType(MultipartFile file) {
        if (!allowedTypes.containsKey(file.getContentType()))
            throw new BadRequestException("File is not valid format");
    }

    Resource loadFileByName(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
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

    abstract void setAllowedTypes();
}