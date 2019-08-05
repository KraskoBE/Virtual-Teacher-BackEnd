package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.CourseResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public ResponseEntity findAll(@PageableDefault Pageable pageable)
    {
        return ResponseEntity.ok().body(
                courseService.findAll(pageable)
                        .map(course -> modelMapper.map(
                                course,
                                CourseResponseDTO.class)
                        )
        );
    }

    @GetMapping("/top")
    public ResponseEntity findOrderedByAverageRating(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(
                courseService.findByOrderedByAverageRating(pageable)
                        .map(course -> modelMapper.map(
                                course,
                                CourseResponseDTO.class)
                        )
        );
    }

    @GetMapping("/topic/{id}")
    public ResponseEntity findAllByTopic(@PathVariable(name = "id") Long topicId,
                                         @PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(
                courseService.findAllByTopic(topicId, pageable).stream()
                        .map(course -> modelMapper.map(course, CourseResponseDTO.class))
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Student')")
    public ResponseEntity findById(@PathVariable(name = "id") final Long courseId,
                                   @CurrentUser final User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.findByIdAndUser(courseId, user),
                        CourseResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Teacher')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save(@Valid @ModelAttribute final CourseRequestDTO course,
                               @CurrentUser final User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.save(course, user),
                        CourseResponseDTO.class)
        );
    }
}
