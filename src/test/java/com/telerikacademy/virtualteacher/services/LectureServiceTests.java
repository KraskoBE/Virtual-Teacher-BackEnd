package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import com.telerikacademy.virtualteacher.services.contracts.TaskService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import com.telerikacademy.virtualteacher.services.contracts.VideoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class LectureServiceTests {

    @Mock
    LectureRepository lectureRepository;
    @Mock
    CourseService courseService;
    @Mock
    UserService userService;
    @Mock
    VideoService videoService;
    @Mock
    TaskService taskService;

    @InjectMocks
    LectureServiceImpl lectureService;

    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        List<Lecture> all = new ArrayList<>();
        Lecture one = new Lecture();
        Lecture two = new Lecture();
        all.add(one);
        all.add(two);

        when(lectureRepository.findAll()).thenReturn(all);

        //Act & Assert
        Assert.assertEquals(lectureService.findAll(), all);
    }

    @Test (expected = NotFoundException.class)
    public void findById_Should_ThrowException_When_NotFound() {
        //Arrange
        when(lectureRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        lectureService.findById(1L);
    }

    @Test
    public void findById_Should_Return_Lecture_When_Found() {
        //Arrange
        Lecture lecture = new Lecture();
        lecture.setId(1L);

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

        //Act & Assert
        Assert.assertEquals(lectureService.findById(1L), lecture);
    }

    @Test
    public void findAllByCourse_Should_ReturnAll() {
        //Arrange
        List<Lecture> all = new ArrayList<>();
        Lecture one = new Lecture();
        Lecture two = new Lecture();
        all.add(one);
        all.add(two);

        Course course = new Course();
        course.setId(1L);

        when(lectureRepository.findAllByCourse_Id(course.getId())).thenReturn(all);

        //Act & Assert
        Assert.assertEquals(lectureService.findAllByCourse(course), all);
    }

    @Test (expected = NotFoundException.class)
    public void findByCourseAndInnerId_Should_ThrowException_When_NotFound() {
        //Arrange
        User user = new User();

        Course course = new Course();
        course.setId(1L);

        when(courseService.findById(course.getId())).thenReturn(course);


        //Act & Assert
        lectureService.findByCourseAndInnerId(user, course.getId(), 1L);
    }

    @Test (expected = AccessDeniedException.class)
    public void findByCourseAndInnerId_Should_ThrowException_When_UserIsNotEnrolled() {
        //Arrange
        User user = new User();
        Long courseId = 1L;
        Long lectureId = 1L;
        Course course = new Course();
        course.setId(courseId);
        Lecture lecture = new Lecture();
        lecture.setInnerId(lectureId);
        course.getLectures().add(lecture);

        when(courseService.findById(courseId)).thenReturn(course);
        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);

        //Act & Assert
        lectureService.findByCourseAndInnerId(user, courseId, lectureId);
    }

    @Test (expected = NotFoundException.class)
    public void findByCourseAndInnerId_Should_ThrowException_When_PreviousLectureIsNotFound() {
        //Arrange
        User user = new User();
        Long courseId = 1L;
        Long lectureId = 2L;
        Course course = new Course();
        course.setId(courseId);
        Lecture lecture = new Lecture();
        lecture.setInnerId(lectureId);
        Lecture previous = new Lecture();
        course.getLectures().add(lecture);
        course.getUsers().add(user);
        when(courseService.findById(courseId)).thenReturn(course);
        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);

        //Act & Assert
        lectureService.findByCourseAndInnerId(user, courseId, lectureId);
    }

    @Test (expected = AccessDeniedException.class)
    public void findByCourseAndInnerId_Should_ThrowException_When_PreviousLectureIsNotFinished() {
        //Arrange
        User user = new User();
        Long courseId = 1L;
        Long lectureId = 2L;
        Course course = new Course();
        course.setId(courseId);
        Lecture lecture = new Lecture();
        lecture.setInnerId(lectureId);
        Lecture previous = new Lecture();
        previous.setInnerId(1L);
        course.getLectures().add(previous);
        course.getLectures().add(lecture);
        course.getUsers().add(user);
        when(courseService.findById(courseId)).thenReturn(course);
        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);

        //Act & Assert
        lectureService.findByCourseAndInnerId(user, courseId, lectureId);
    }

    @Test
    public void findByCourseAndInnerId_Should_Return_When_Successful() {
        //Arrange
        User user = new User();
        Long courseId = 1L;
        Long lectureId = 2L;
        Course course = new Course();
        course.setId(courseId);
        Lecture lecture = new Lecture();
        lecture.setInnerId(lectureId);
        Lecture previous = new Lecture();
        previous.setInnerId(1L);
        course.getLectures().add(previous);
        course.getLectures().add(lecture);
        course.getUsers().add(user);
        user.getFinishedLectures().add(previous);
        when(courseService.findById(courseId)).thenReturn(course);
        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);

        //Act
        Lecture returned = lectureService.findByCourseAndInnerId(user,courseId,lectureId);

        //Assert
        Assert.assertEquals(returned,lecture);
    }

    @Test( expected = AlreadyExistsException.class)
    public void save_Should_ThrowException_When_LectureNameIsTaken() {
        //Arrange
        final String lectureName = "Test Name";

        LectureRequestDTO lectureRequestDTO = new LectureRequestDTO();
        User author = new User();

        lectureRequestDTO.setName(lectureName);

        Lecture lecture = new Lecture();
        lecture.setName(lectureName);

        when(lectureRepository.findByNameIgnoreCase(lectureName)).thenReturn(Optional.of(lecture));

        //Act & Assert
        lectureService.save(lectureRequestDTO, author);
    }

    @Test( expected = BadRequestException.class)
    public void save_Should_ThrowException_When_CourseIsAlreadySubmitted() {
        //Arrange
        final String lectureName = "Test Name";
        final Long courseId = 1L;

        LectureRequestDTO lectureRequestDTO = new LectureRequestDTO();
        User author = new User();
        lectureRequestDTO.setName(lectureName);
        Lecture lecture = new Lecture();
        lecture.setName(lectureName);
        Course course = new Course();
        course.setId(courseId);
        course.setSubmitted(true);
        lectureRequestDTO.setCourseId(courseId);

        when(courseService.findById(lectureRequestDTO.getCourseId())).thenReturn(course);
        when(lectureRepository.findByNameIgnoreCase(lectureName)).thenReturn(Optional.empty());

        //Act & Assert
        lectureService.save(lectureRequestDTO, author);
    }

    @Test
    public void save_Should_Return_Lecture_When_Successful() {
        //Arrange
        final String lectureName = "Test Name";
        final String lectureDescription = "Lecture test description";
        final Long courseId = 1L;
        final Long authorId = 1L;
        final Long lectureId = 1L;

        LectureRequestDTO lectureRequestDTO = new LectureRequestDTO();
        User author = new User();
        author.setId(authorId);
        lectureRequestDTO.setName(lectureName);
        lectureRequestDTO.setDescription(lectureDescription);
        Lecture lecture = new Lecture();
        lecture.setName(lectureName);
        lecture.setId(lectureId);
        Course course = new Course();
        course.setId(courseId);
        course.setSubmitted(false);
        lectureRequestDTO.setCourseId(courseId);
        byte[] content = new byte[20];
        new Random().nextBytes(content);
        MockMultipartFile file = new MockMultipartFile("file", content);
        Video video = new Video();
        Task task = new Task();
        lectureRequestDTO.setVideoFile(file);
        lectureRequestDTO.setTaskFile(file);

        lecture.setDescription(lectureDescription);
        lecture.setVideo(video);
        lecture.setTask(task);
        lecture.setInnerId(1L);
        lecture.setAuthor(author);
        lecture.setCourse(course);

        when(courseService.findById(lectureRequestDTO.getCourseId())).thenReturn(course);
        when(lectureRepository.findByNameIgnoreCase(lectureName)).thenReturn(Optional.empty());
        when(videoService.save(author.getId(), lecture.getId(), lectureRequestDTO.getVideoFile())).thenReturn(video);
        when(taskService.save(author.getId(), lecture.getId(), lectureRequestDTO.getTaskFile())).thenReturn(task);
        when(lectureRepository.save(Mockito.isA(Lecture.class))).thenReturn(lecture);

        //Act
        Lecture result = lectureService.save(lectureRequestDTO, author);

        //Assert
        Assert.assertEquals(result, lecture);
    }
}














