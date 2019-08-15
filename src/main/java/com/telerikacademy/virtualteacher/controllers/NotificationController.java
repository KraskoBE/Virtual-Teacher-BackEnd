package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.NotificationService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasRole('Student')")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/unseen")
    public ResponseEntity getUserUnseenNotifications(@CurrentUser final User user) {
        return ResponseEntity.ok().body(notificationService.getUserUnseenNotifications(user));
    }

    @PutMapping("/see/{id}")
    public ResponseEntity seeNotification(@PathVariable("id") final Long id) {
        return ResponseEntity.ok().body(notificationService.markAsSeen(id));
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/send")
    public ResponseEntity sendNotification(@RequestParam("receiverId") final Long receiverId,
                                           @RequestParam("message") final String message) {
        return ResponseEntity.ok().body(notificationService.sendNotification(userService.findById(receiverId), message));
    }
}
