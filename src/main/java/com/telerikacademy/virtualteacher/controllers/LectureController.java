package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/lectures")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;


    //TODO still getting unsupported media types here
    @PostMapping
    public ResponseEntity save(@Valid @RequestPart LectureRequestDTO lecture,
                               @CurrentUser User user,
                               @RequestParam(value = "videoFile") MultipartFile videoFile,
                               @RequestParam(value = "taskFile") MultipartFile taskFile) {

        return lectureService.save(lecture,user,videoFile,taskFile)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.badRequest().build());

    }
}
