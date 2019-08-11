package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.exceptions.storage.StorageException;
import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.PictureRepository;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PictureServiceTests {

    @Mock
    PictureRepository pictureRepository;
    @Mock
    UserService userService;

    @InjectMocks
    PictureServiceImpl pictureService;

    @Test (expected = StorageException.class)
    public void save_Should_ThrowException_When_FileIsEmpty() {
    //Arrange
    final Long authorId = 1L;
    final Long userId = authorId;
    User author = new User();
    author.setId(authorId);
    byte[] content = null;
    final String name = "picture.jpg";
    final String type = "image/jpeg";
    MockMultipartFile file = new MockMultipartFile(name, name, type,content);

    when(userService.findById(authorId)).thenReturn(author);

    //Act & Assert
    pictureService.save(authorId, userId, file);

    }

    @Test
    public void save_Should_Return_Picture_When_Successful() {
        //Arrange
        final Long authorId = 1L;
        final Long userId = authorId;
        User author = new User();
        author.setId(authorId);
        byte[] content = new byte[20];
        new Random().nextBytes(content);
        final String name = "picture.jpg";
        final String type = "image/jpeg";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);

        Optional<Picture> picture = Optional.of(new Picture());

        when(userService.findById(authorId)).thenReturn(author);
        when(pictureRepository.findByFilePath(Mockito.isA(String.class))).thenReturn(picture);
        when(pictureRepository.save(Mockito.isA(Picture.class))).thenReturn(picture.get());
        //Act
        Picture result = pictureService.save(authorId,userId,file);

        //Assert
        Assert.assertEquals(result,picture.get());
    }

    @Test (expected = NotFoundException.class)
    public void findByUserId_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.findById(userId)).thenReturn(user);

        //Act & Assert
        pictureService.findByUserId(userId);
    }


    @Test
    public void findByUserId_Should_Return_Resource_When_Successful() {
        //Arrange
        final Long userId = 1L;
        final String fileName = "Picture";
        User user = new User();
        user.setId(userId);
        Picture picture = new Picture();
        picture.setFileName(fileName);

        Resource resource = null;

        when(userService.findById(userId)).thenReturn(user);
        when(pictureService.loadFileByName(fileName)).thenReturn(resource);

        //Act
        Resource result = pictureService.findByUserId(userId);

        //Assert
        Assert.assertEquals(result, resource);
    }
}
