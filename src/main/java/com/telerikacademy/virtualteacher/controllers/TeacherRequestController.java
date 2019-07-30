package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
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
    @PostMapping
    public ResponseEntity<TeacherRequest> accept(@RequestParam(value = "teacherRequestId", required = false) Long teacherRequestId,
                                                 @RequestParam(value = "userId", required = false) Long userId) {

        if (teacherRequestId != null) {
            return teacherRequestService.acceptById(teacherRequestId)
                    .map(record -> ResponseEntity.ok().body(record))
                    .orElseThrow(() -> new BadRequestException("Cannot be accepted"));
        } else if (userId != null) {
            return teacherRequestService.acceptByUserId(userId)
                    .map(record -> ResponseEntity.ok().body(record))
                    .orElseThrow(() -> new BadRequestException("Cannot be accepted"));
        } else {
            throw new NotFoundException("TeacherRequest not found");
        }

    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping
    public void deny(@RequestParam(value = "teacherRequestId", required = false) Long teacherRequestId,
                                                 @RequestParam(value = "userId", required = false) Long userId) {

        if (teacherRequestId != null) {
            teacherRequestService.deleteById(teacherRequestId);
        } else if (userId != null) {
            teacherRequestService.deleteByUserId(userId);
        } else {
            throw new NotFoundException("TeacherRequest not found");
        }

    }

}
