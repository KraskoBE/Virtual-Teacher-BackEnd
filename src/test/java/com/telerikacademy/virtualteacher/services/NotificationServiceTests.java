package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Notification;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.NotificationRepository;
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
public class NotificationServiceTests {

    @Mock
    NotificationRepository notificationRepository;

    @InjectMocks
    NotificationServiceImpl notificationService;

    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        List<Notification> all = new ArrayList<>();
        Notification one = new Notification();
        Notification two = new Notification();
        all.add(one);
        all.add(two);

        when(notificationRepository.findAll()).thenReturn(all);

        //Act & Assert
        Assert.assertEquals(notificationService.findAll(), all);
    }

    @Test (expected = NotFoundException.class)
    public void findById_Should_ThrowException_When_NotFound() {
        //Arrange
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        //Act & Assert
        notificationService.findById(1L);
    }

    @Test
    public void findById_Should_Return_Notification_When_Found() {
        //Arrange
        Notification notification = new Notification();
        notification.setId(1L);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        //Act & Assert
        Assert.assertEquals(notificationService.findById(1L), notification);
    }

    @Test
    public void findAllByUser_Should_Return_NotificationList_When_Found() {
        //Arrange
        List<Notification> all = new ArrayList<>();
        Notification one = new Notification();
        Notification two = new Notification();
        all.add(one);
        all.add(two);
        User user = new User();

        when(notificationRepository.findByReceiver(user)).thenReturn(all);

        //Act & Assert
        Assert.assertEquals(notificationService.findAllByUser(user), all);
    }

    @Test
    public void save_Should_Return_Notification_When_Successful() {
        //Arrange
        Notification notification = new Notification();

        when(notificationRepository.save(notification)).thenReturn(notification);

        //Act
        Notification result = notificationService.save(notification);

        //Assert
        Assert.assertEquals(result, notification);
    }

    @Test
    public void sendNotification_Should_Return_Notification_When_Successful() {
        //Arrange
        Notification notification = new Notification();
        User receiver = new User();
        final String message = "message";

        when(notificationRepository.save(Mockito.isA(Notification.class))).thenReturn(notification);

        //Act
        Notification result = notificationService.sendNotification(receiver,message);

        //Assert
        Assert.assertEquals(result, notification);
    }

    @Test (expected = NotFoundException.class)
    public void markAsSeen_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long notificationId = 1L;
        Notification notification = new Notification();
        notification.setId(notificationId);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        //Act & Assert
        notificationService.markAsSeen(notificationId);
    }

    @Test
    public void markAsSeen_Should_Return_Notification_When_Successful() {
        //Arrange
        final Long notificationId = 1L;
        Notification notification = new Notification();
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        notification.setSeen(true);
        when(notificationRepository.save(notification)).thenReturn(notification);

        //Act
        Notification result = notificationService.markAsSeen(notificationId);

        //Assert
        Assert.assertEquals(result,notification);
    }

    @Test
    public void getUserUnseenNotifications_Should_Return_NotificationList() {
        //Arrange
        List<Notification> all = new ArrayList<>();
        Notification one = new Notification();
        Notification two = new Notification();
        all.add(one);
        all.add(two);
        User user = new User();

        when(notificationRepository.findByReceiver(user)).thenReturn(all);

        //Act
        List<Notification> result = notificationService.getUserUnseenNotifications(user);

        //Assert
        Assert.assertEquals(result,all);
    }


}
