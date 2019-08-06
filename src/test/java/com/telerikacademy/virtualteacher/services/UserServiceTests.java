package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.RoleRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        List<User> users = new ArrayList<>();
        users.add(new User("email1@email.com",
                "password",
                "Petur",
                "Petrov",
                LocalDate.now()));
        users.add(new User("email2@email.com",
                "password",
                "Ivan",
                "Ivanov",
                LocalDate.now()));

        //Act
        when(userRepository.findAll()).thenReturn(users);

        //Assert
        Assert.assertEquals(users, userService.findAll());
    }

    @Test(expected = NotFoundException.class)
    public void findById_Should_ThrowNotFound_When_UserIsNotFound() {
        //Act
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert
        userService.findById(1L);
    }


    @Test
    public void save_Should_SaveUser_When_EmailIsNotAlreadyTaken() {
        //Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@email.com",
                "password123",
                "Ivan",
                "Ivanov",
                LocalDate.now());

        User user = new User(userRequestDTO.getEmail(), userRequestDTO.getPassword(), userRequestDTO.getFirstName(), userRequestDTO.getLastName(), userRequestDTO.getBirthDate());
        user.setId(1L);

        Role studentRole = new Role(Role.Name.Student);

        //Act
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(Role.Name.Student.toString())).thenReturn(Optional.of(studentRole));
        userService.save(userRequestDTO);

        //Assert
        verify(userRepository,times(2)).save(user);

    }

}
