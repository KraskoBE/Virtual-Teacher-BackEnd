package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.Notification;
import com.telerikacademy.virtualteacher.models.User;

import java.util.List;

public interface NotificationService {

    List<Notification> findAll();

    Notification findById(Long id);

    List<Notification> findByUser(User user);

    Notification save(Notification notification);

    Notification sendNotification(User receiver, String message);

    Notification markAsSeen(Long id);
}
