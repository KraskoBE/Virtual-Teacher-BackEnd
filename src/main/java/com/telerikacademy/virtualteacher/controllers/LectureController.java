package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.CourseService;
import com.telerikacademy.virtualteacher.services.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/lectures")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;
    private final CourseService courseService;

    
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save(@Valid @ModelAttribute LectureRequestDTO lectureRequestDTO,
                                   @CurrentUser User user) {

        return lectureService.save(lectureRequestDTO, user)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.badRequest().build());

    }
}
