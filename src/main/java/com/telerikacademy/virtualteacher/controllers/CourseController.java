package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.CourseResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
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
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public ResponseEntity findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(
                courseService.findAll(pageable)
                        .map(course -> modelMapper.map(
                                course,
                                CourseResponseDTO.class)
                        )
        );
    }

    @GetMapping("/search")
    public ResponseEntity searchForOne(@RequestParam("searchField") String searchField){
        return ResponseEntity.ok().body(
                courseService.searchName(searchField)
        );
    }

    @GetMapping("/recent")
    public ResponseEntity findAllOrderedById(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(
                courseService.findAllOrderedByIdDesc(pageable)
                        .map(course -> modelMapper.map(
                                course,
                                CourseResponseDTO.class)
                        )
        );
    }

    @GetMapping("/top")
    public ResponseEntity findAllOrderedByAverageRating(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(
                courseService.findAllByOrderedByAverageRating(pageable)
                        .map(course -> modelMapper.map(
                                course,
                                CourseResponseDTO.class)
                        )
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PostMapping("/enroll")
    public ResponseEntity enrollCourse(@RequestParam("courseId") final Long courseId,
                                       @CurrentUser User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.enrollCourse(user, courseId),
                        CourseResponseDTO.class)
        );
    }

    @GetMapping("/topic/{id}")
    public ResponseEntity findAllByTopicOrderedByAverageRating(@PathVariable(name = "id") Long topicId,
                                                               @PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(
                courseService.findAllByTopicOrderedByAverageRatingDesc(topicId, pageable)
                        .map(course -> modelMapper.map(
                                course,
                                CourseResponseDTO.class)
                        )
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

    @PreAuthorize("hasRole('Teacher')")
    @PutMapping("/{courseId}/submit")
    public ResponseEntity submitCourse(@PathVariable Long courseId,
                                       @CurrentUser User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.submit(courseId, user),
                        CourseResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/rate_course")
    public ResponseEntity rateCourse(@RequestParam("course_id") final Long courseId,
                                     @RequestParam("rating") final Integer rating,
                                     @CurrentUser final User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.rate(user, courseId, rating),
                        CourseResponseDTO.class)
        );
    }
}
