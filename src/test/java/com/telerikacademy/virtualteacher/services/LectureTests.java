package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class LectureTests {

    @Mock
    LectureRepository lectureRepository;
    @Mock
    CourseService courseService;

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
}














