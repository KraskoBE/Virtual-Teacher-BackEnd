package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.CourseResponseDTO;
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
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.findByIdAndUser(courseId, user),
                        CourseResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Teacher')")
    @PostMapping
    public ResponseEntity save(@Valid @RequestBody CourseRequestDTO course,
                               @CurrentUser User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.save(course, user),
                        CourseResponseDTO.class)
        );
    }
}
