package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
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


    //TODO still getting unsupported media types here
    @PostMapping
    public ResponseEntity save(@Valid @RequestPart LectureRequestDTO lecture,
                               @RequestParam( value = "courseId" ) Long courseId,
                               @CurrentUser User user,
                               @RequestParam(value = "videoFile") MultipartFile videoFile,
                               @RequestParam(value = "taskFile") MultipartFile taskFile) {

        Optional<Course> course = courseService.findById(courseId);
        if (!course.isPresent()) throw new NotFoundException("Course could not be found ");


        return lectureService.save(course.get(), lecture,user,videoFile,taskFile)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.badRequest().build());

    }
}
