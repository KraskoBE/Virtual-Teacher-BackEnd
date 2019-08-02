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





}
