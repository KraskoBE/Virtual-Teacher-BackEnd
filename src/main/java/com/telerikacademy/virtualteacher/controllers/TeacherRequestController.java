package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.TeacherRequest;
import com.telerikacademy.virtualteacher.services.TeacherRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/teacher_requests")
public class TeacherRequestController {

    private final TeacherRequestService teacherRequestService;

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{userId}")
    public ResponseEntity<TeacherRequest> accept(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(
                teacherRequestService.acceptByUserId(userId)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{userId}")
    public void deny(@PathVariable(name = "userId") Long userId) {
        teacherRequestService.deleteByUserId(userId);
    }

}
