package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Notification;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.NotificationRepository;
import com.telerikacademy.virtualteacher.services.contracts.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("NotificationService")
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
    }

    @Override
    public List<Notification> findByUser(User user) {
        return notificationRepository.findAll()
                .stream()
                .filter(x -> x.getReceiver().equals(user))
                .collect(Collectors.toList());
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification sendNotification(User receiver, String message) {
        Notification newNotification = new Notification(receiver, message);
        return notificationRepository.save(newNotification);
    }

    @Override
    public Notification markAsSeen(Long id) {
        Notification toMark = findById(id);
        toMark.setSeen(true);
        return  notificationRepository.save(toMark);
    }
}
