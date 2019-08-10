package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.models.Video;
import com.telerikacademy.virtualteacher.repositories.VideoRepository;
import com.telerikacademy.virtualteacher.services.contracts.LectureService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import com.telerikacademy.virtualteacher.services.contracts.VideoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class VideoServiceTests {

    @Mock
    VideoRepository videoRepository;
    @Mock
    UserService userService;
    @Mock
    LectureService lectureService;

    @InjectMocks
    VideoServiceImpl videoService;

    /*
    @Test
    public void updatePicture_Should_saveUser_When_Successful() {
        //Arrange
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        Lecture lecture = new Lecture();

        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "video.mp4";
        final String type = "video/mp4";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Video video = new Video(author, lecture, name, type, file.getSize(), name);

        //Act
        Mockito.when(userService.findById(1L)).thenReturn(author);
        Mockito.when(lectureService.findById(1L)).thenReturn(lecture);
        userService.updatePicture(1L, author ,file);

        //Assert
        //Mockito.verify(videoRepository, times(1)).save();
    }*/
}
