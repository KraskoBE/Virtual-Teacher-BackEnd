package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Thumbnail;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.ThumbnailRepository;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
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
public class ThumbnailServiceTests {

    @Mock
    ThumbnailRepository thumbnailRepository;
    @Mock
    UserService userService;
    @Mock
    CourseService courseService;

    @InjectMocks
    ThumbnailServiceImpl thumbnailService;



    @Test
    public void save_Should_Return_Thumbnail_When_NewFileIsUploaded() {
        //Arrange
        final Long authorId = 0L;
        final Long courseId = 0L;

        byte[] content = new byte[20];
        new Random().nextBytes(content);
        final String name = "picture.jpg";
        final String type = "image/jpeg";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);

        User author = new User();
        when(userService.findById(authorId)).thenReturn(author);

        Course course = new Course();
        when(courseService.findById(courseId)).thenReturn(course);

        Thumbnail thumbnail = new Thumbnail();

        when(thumbnailRepository.findByFilePath(Mockito.isA(String.class))).thenReturn(Optional.empty());
        when(thumbnailRepository.save(Mockito.isA(Thumbnail.class))).thenReturn(thumbnail);

        //Act
        Thumbnail result = thumbnailService.save(authorId, courseId, file);

        //Assert
        Assert.assertEquals(result, thumbnail);
    }

    @Test (expected = NotFoundException.class)
    public void findCourseById_Should_ThrowException_When_PictureNotFound() {
        //Arrange
        final Long courseId = 0L;
        Course course = new Course();
        when(courseService.findById(courseId)).thenReturn(course);

        //Act & Arrange
        thumbnailService.findByCourseId(courseId);
    }

    @Test
    public void findCourseById_Should_Return_Resource_When_Successful() {
        //Arrange
        final Long courseId = 0L;
        final String fileName = "thumbnail.jpg";
        Course course = new Course();
        when(courseService.findById(courseId)).thenReturn(course);
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setFileName(fileName);
        course.setThumbnail(thumbnail);

        Resource resource = thumbnailService.loadFileByName(fileName);

        //Act
        Resource result = thumbnailService.findByCourseId(courseId);

        //Assert
        Assert.assertEquals(result, resource);
    }

}

