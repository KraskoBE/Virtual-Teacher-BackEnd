package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.models.Video;
import com.telerikacademy.virtualteacher.repositories.VideoRepository;
import com.telerikacademy.virtualteacher.services.contracts.LectureService;
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

@RunWith(MockitoJUnitRunner.class)
public class VideoServiceTests {

    @Mock
    VideoRepository videoRepository;
    @Mock
    UserService userService;
    @Mock
    LectureService lectureService;

    @InjectMocks
    VideoServiceImpl videoService;

    @Test
    public void save_Should_Return_Video_When_Successful() {
        //Arrange
        final Long authorId = 1L;
        final Long lecureId = 1L;
        final Long videoId = 0L;

        byte[] content = new byte[20];
        new Random().nextBytes(content);
        final String name = "video.mp4";
        final String type = "video/mp4";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);

        User author = new User();
        Lecture lecture = new Lecture();
        Video video = new Video();
        video.setId(videoId);

        when(userService.findById(authorId)).thenReturn(author);
        when(lectureService.findById(lecureId)).thenReturn(lecture);
        //when(videoRepository.findByFilePath(Mockito.anyString())).thenReturn(Optional.of(video));
        when(videoRepository.save(Mockito.isA(Video.class))).thenReturn(video);

        //Act
        Video result = videoService.save(authorId, lecureId, file);

        //Assert
        Assert.assertEquals(result, video);
    }

    @Test (expected = NotFoundException.class)
    public void findByLectureId_Should_ThrowException_When_VideoIsNotFound(){
        //Arrange
        final Long lectureId = 1L;
        Lecture lecture = new Lecture();

        when(lectureService.findById(lectureId)).thenReturn(lecture);
        when(videoRepository.findByLecture(lecture)).thenReturn(Optional.empty());

        //Act & Assert
        videoService.findByLectureId(lectureId);
    }

    @Test
    public void findByLectureId_Should_Return_Resource_When_Successful(){
        //Arrange
        final Long lectureId = 1L;
        Lecture lecture = new Lecture();
        Video video = new Video();
        Resource resource = null;

        when(lectureService.findById(lectureId)).thenReturn(lecture);
        when(videoRepository.findByLecture(lecture)).thenReturn(Optional.of(video));
        when(videoService.loadFileByName(Mockito.any())).thenReturn(resource);

        //Act
        Resource result = videoService.findByLectureId(lectureId);

        //Assert
        Assert.assertEquals(resource, result);
    }
}
