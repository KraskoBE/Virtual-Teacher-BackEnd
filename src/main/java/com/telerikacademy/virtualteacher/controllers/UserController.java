package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.response.CourseResponseDTO;
import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.CourseService;
import com.telerikacademy.virtualteacher.services.TeacherRequestService;
import com.telerikacademy.virtualteacher.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CourseService courseService;
    private final TeacherRequestService teacherRequestService;

    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public List findAll() {
        return userService.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity findById(@PathVariable final Long id) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.findById(id),
                        UserResponseDTO.class)
        );
    }

    @PutMapping("/rate_course")
    public ResponseEntity rateCourse(@RequestParam("course_id") final Long courseId,
                                             @RequestParam("rating") final Integer rating,
                                             @CurrentUser User user) {

        return courseService.rate(user.getId(), courseId, rating)
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new BadRequestException("Rating must be between 1 and 5"));

//        return ResponseEntity.ok().body(courseService.rate(user.getId(), courseId, rating).get());
    }

    @PostMapping("/enroll")
    public ResponseEntity enrollCourse(@RequestParam("courseId") Long courseId,
                                       @RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.enrollCourse(userId, courseId),
                        CourseResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PostMapping("/teacherRequest")
    public ResponseEntity teacherRequest(@CurrentUser User user) {
        return ResponseEntity.ok().body(
                teacherRequestService.save(user)
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/{id}/updatePicture")
    public ResponseEntity changePicture(@PathVariable(name = "id") Long userId,
                                        @CurrentUser User user,
                                        @RequestParam(name = "picture") MultipartFile pictureFile) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.updatePicture(userId, user, pictureFile),
                        UserResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        userService.deleteById(id);
    }

}
