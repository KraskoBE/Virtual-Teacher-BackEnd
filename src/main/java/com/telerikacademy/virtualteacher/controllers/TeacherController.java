package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.TeacherRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
@RestController
@RequestMapping("/api/teachers")
@AllArgsConstructor
@PreAuthorize("hasRole('Teacher')")
public class TeacherController {

    private final TeacherRequestService teacherRequestService;

    @PreAuthorize("hasRole('Student')")
    @PostMapping
    public ResponseEntity teacherRequest(@CurrentUser final User user) {
        return ResponseEntity.ok().body(
                teacherRequestService.save(user)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{userId}")
    public ResponseEntity accept(@PathVariable(name = "userId") final Long userId) {
        return ResponseEntity.ok().body(
                teacherRequestService.acceptByUserId(userId)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{userId}")
    public void deny(@PathVariable(name = "userId") final Long userId) {
        teacherRequestService.deleteByUserId(userId);
    }

}
