package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.TeacherRequest;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.TeacherRequestRepository;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TeacherRequestServiceTests {

    @Mock
    TeacherRequestRepository teacherRequestRepository;
    @Mock
    UserService userService;

    @InjectMocks
    TeacherRequestServiceImpl teacherRequestService;

    @Test (expected = AlreadyExistsException.class)
    public void save_Should_ThrowException_When_ThisTeacherRequestAlreadyExists() {
        //Arrange
        User user = new User();
        TeacherRequest teacherRequest = new TeacherRequest();

        when(teacherRequestRepository.findByUser(user)).thenReturn(Optional.of(teacherRequest));


        //Act & Assert
        teacherRequestService.save(user);
    }

    @Test
    public void save_Should_Return_TeacherRequest_When_Successful() {
        //Arrange
        User user = new User();
        TeacherRequest teacherRequest = new TeacherRequest();

        when(teacherRequestRepository.findByUser(user)).thenReturn(Optional.empty());
        when(teacherRequestRepository.save(Mockito.isA(TeacherRequest.class))).thenReturn(teacherRequest);

        //Act
        TeacherRequest result = teacherRequestService.save(user);

        //Assert
        Assert.assertEquals(result, teacherRequest);
    }

    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        List<TeacherRequest> all = new ArrayList<>();
        TeacherRequest one = new TeacherRequest();
        TeacherRequest two = new TeacherRequest();
        all.add(one);
        all.add(two);

        when(teacherRequestRepository.findAll()).thenReturn(all);

        //Act
        List<TeacherRequest> result = teacherRequestService.findAll();

        //Act & Assert
        Assert.assertEquals(result, all);
    }

    @Test (expected = NotFoundException.class)
    public void findById_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long id = 1L;

        when(teacherRequestRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        teacherRequestService.findById(id);
    }

    @Test
    public void findById_Should_Return_TeacherRequest_When_Successful() {
        //Arrange
        final Long id = 1L;
        TeacherRequest teacherRequest = new TeacherRequest();
        when(teacherRequestRepository.findById(id)).thenReturn(Optional.of(teacherRequest));

        //Act
        TeacherRequest result = teacherRequestService.findById(id);

        //Assert
        Assert.assertEquals(result,teacherRequest);
    }

    @Test (expected = NotFoundException.class)
    public void findByUserId_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long userId = 1L;
        User user = new User();

        when(userService.findById(userId)).thenReturn(user);
        when(teacherRequestRepository.findByUser(user)).thenReturn(Optional.empty());

        //Act & Assert
        teacherRequestService.findByUserId(userId);
    }

    @Test
    public void findByUserId_Should_Return_TeacherRequest_When_Successful() {
        //Arrange
        final Long userId = 1L;
        User user = new User();
        TeacherRequest teacherRequest = new TeacherRequest();

        when(userService.findById(userId)).thenReturn(user);
        when(teacherRequestRepository.findByUser(user)).thenReturn(Optional.of(teacherRequest));

        //Act
        TeacherRequest result = teacherRequestService.findByUserId(userId);

        //Assert
        Assert.assertEquals(result,teacherRequest);
    }

    @Test (expected = BadRequestException.class)
    public void acceptByUserId_Should_ThrowException_When_UserIsAlreadyTeacher() {
        //Arrange
        final Long userId = 1L;
        TeacherRequest teacherRequest = new TeacherRequest();
        User user = new User();

        when(userService.findById(userId)).thenReturn(user);
        when(teacherRequestRepository.findByUser(user)).thenReturn(Optional.of(teacherRequest));
        when(userService.hasRole(user, Role.Name.Teacher)).thenReturn(true);

        //Act & Assert
        teacherRequestService.acceptByUserId(userId);

    }

    @Test
    public void acceptByUserId_Should_Return_TeacherRequest_When_Successful() {
        //Arrange
        final Long userId = 1L;
        TeacherRequest teacherRequest = new TeacherRequest();
        User user = new User();

        when(userService.findById(userId)).thenReturn(user);
        when(teacherRequestRepository.findByUser(user)).thenReturn(Optional.of(teacherRequest));
        when(userService.hasRole(user, Role.Name.Teacher)).thenReturn(false);
        when(teacherRequestRepository.save(teacherRequest)).thenReturn(teacherRequest);

        //Act
        TeacherRequest result = teacherRequestService.acceptByUserId(userId);

        //Assert
        Assert.assertEquals(result,teacherRequest);
    }
}
