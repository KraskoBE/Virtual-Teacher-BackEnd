package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.CourseResponseDTO;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.CourseService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('Student')")
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable(name = "id") Long courseId,
                                   @CurrentUser User user) {
        return courseService.findByIdAndUser(courseId, user)
                .map(record -> modelMapper.map(record, CourseResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @PreAuthorize("hasRole('Teacher')")
    @PostMapping
    public ResponseEntity save(@Valid @RequestBody CourseRequestDTO course,
                               @CurrentUser User user) {
        return courseService.save(course, user)
                .map(record -> modelMapper.map(record, CourseResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/enroll")
    public ResponseEntity enrollCourse(@RequestParam("courseId") Long courseId,
                                        @CurrentUser User user){
        return courseService.enroll(courseId, user)
                .map(record-> modelMapper.map(record, CourseResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(()-> new BadRequestException("Couldn't enroll course"));
    }

}
