package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test( expected = UserNotFoundException.class)
    public void loadUserByUsername_Should_ThrowException_When_NotFound() {
        //Arrange
        final String username = "username@abv.bg";
        User user = new User();

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        //Act & Assert
        userDetailsService.loadUserByUsername(username);
    }

    @Test
    public void loadUserByUsername_Should_Return_UserDetails_When_Successful() {
        //Arrange
        final String username = "username@abv.bg";
        User user = new User();

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        //Act
        UserDetails result = userDetailsService.loadUserByUsername(username);

        //Assert
        Assert.assertEquals(result, user);
    }
}
