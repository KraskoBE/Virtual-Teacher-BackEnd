package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.NotificationService;
import com.telerikacademy.virtualteacher.services.contracts.TeacherRequestService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@AllArgsConstructor
@PreAuthorize("hasRole('Teacher')")
public class TeacherController {

    private final TeacherRequestService teacherRequestService;
    private final NotificationService notificationService;
    private final UserService userService;

    @PreAuthorize("hasRole('Student')")
    @PostMapping
    public ResponseEntity teacherRequest(@CurrentUser final User user) {
        return ResponseEntity.ok().body(
                teacherRequestService.save(user)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public ResponseEntity getTeacherRequests() {
        return ResponseEntity.ok().body(
                teacherRequestService.findAll()
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{userId}")
    public ResponseEntity accept(@PathVariable(name = "userId") final Long userId) {

        notificationService.sendNotification(userService.findById(userId), "Your teacher request has been accepted");

        return ResponseEntity.ok().body(
                teacherRequestService.acceptByUserId(userId)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{userId}")
    public void deny(@PathVariable(name = "userId") final Long userId) {
        notificationService.sendNotification(userService.findById(userId), "Your teacher request has been denied");

        teacherRequestService.deleteByUserId(userId);
    }
}
