package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
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
public class AssignmentServiceTests {

    @Mock
    AssignmentRepository assignmentRepository;
    @Mock
    UserService userService;
    @Mock
    LectureService lectureService;

    @InjectMocks
    AssignmentServiceImpl assignmentService;

    @Test (expected = NotFoundException.class)
    public void findById_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long id = 0L;

        when(assignmentRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Arrange
        assignmentService.findById(id);
    }

    @Test
    public void findById_Should_Return_Assignment_When_Successful() {
        //Arrange
        final Long id = 0L;
        Assignment assignment = new Assignment();
        when(assignmentRepository.findById(id)).thenReturn(Optional.of(assignment));

        //Act
        Assignment result = assignmentService.findById(id);

        //Assert
        Assert.assertEquals(result, assignment);
    }

    @Test
    public void save_Should_Return_Assignment_When_Successful() {
        //Arrange
        final Long authorId = 0L;
        final Long lectureId = 0L;
        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "assignment.txt";
        final String type = "text/plain";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Assignment assignment  = new Assignment();

        User author = new User();
        when(userService.findById(authorId)).thenReturn(author);

        Lecture lecture = new Lecture();
        when(lectureService.findById(lectureId)).thenReturn(lecture);

        when(assignmentRepository.save(Mockito.any())).thenReturn(assignment);

        //Act
        Assignment result = assignmentService.save(authorId, lectureId, file);

        //Assert
        Assert.assertEquals(result, assignment);
    }

    @Test (expected = NotFoundException.class)
    public void findByLectureIdAndUser_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long lectureId = 0L;
        final Long userId = 0L;

        Lecture lecture = new Lecture();
        when(lectureService.findById(lectureId)).thenReturn(lecture);

        User user = new User();
        when(userService.findById(userId)).thenReturn(user);

        Assignment assignment = new Assignment();
        when(assignmentRepository.findByLectureAndAuthor(lecture, user)).thenReturn(Optional.empty());

        //Act & Assert
        assignmentService.findByLectureIdAndUserId(lectureId, userId);
    }

    @Test
    public void findByLectureIdAndUser_Should_Return_Resource_When_Successful() {
        //Arrange
        final Long lectureId = 0L;
        final Long userId = 0L;

        final String fileName = "assignment.txt";
        Lecture lecture = new Lecture();
        when(lectureService.findById(lectureId)).thenReturn(lecture);

        User user = new User();
        when(userService.findById(userId)).thenReturn(user);

        Assignment assignment = new Assignment();
        assignment.setFileName(fileName);
        when(assignmentRepository.findByLectureAndAuthor(lecture, user)).thenReturn(Optional.of(assignment));

        Resource resource = null;
        when(assignmentService.loadFileByName(fileName)).thenReturn(resource);

        //Act
        Resource result = assignmentService.findByLectureIdAndUserId(lectureId, userId);

        //Assert
        Assert.assertEquals(resource, result);
    }

    @Test
    public void isLastAssignment_Should_Return_True_When_IsLastAssignment() {
        //Arrange
        final Long lectureInnerId = 1L;

        Course course = new Course();
        Lecture lecture = new Lecture();
        lecture.setInnerId(lectureInnerId);
        Assignment assignment = new Assignment();
        course.getLectures().add(lecture);
        lecture.setCourse(course);
        assignment.setLecture(lecture);

        //Act
        boolean result = assignmentService.isLastAssignment(assignment);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isLastAssignment_Should_Return_False_When_IsNotLastAssignment() {
        //Arrange
        final Long lectureInnerId =0L;

        Course course = new Course();
        Lecture lecture = new Lecture();
        Lecture lectureTwo = new Lecture();
        lecture.setInnerId(lectureInnerId);
        lectureTwo.setInnerId(2L);
        Assignment assignment = new Assignment();
        course.getLectures().add(lecture);
        course.getLectures().add(lectureTwo);
        lecture.setCourse(course);
        assignment.setLecture(lecture);

        //Act
        boolean result = assignmentService.isLastAssignment(assignment);

        //Assert
        Assert.assertFalse(result);
    }
}

