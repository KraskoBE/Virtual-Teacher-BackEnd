package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.response.CourseResponseDTO;
import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.AssignmentService;
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


    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable final Long id) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.findById(id),
                        UserResponseDTO.class)
        );
    }

    @PutMapping("/grade_assignment")
    @PreAuthorize("hasRole('Teacher')")
    public ResponseEntity gradeAssignment(@RequestParam("assignment_id") Long assignmentId,
                                          @RequestParam("grade") Integer grade,
                                          @CurrentUser User user) {
        return ResponseEntity.ok().body(userService.gradeAssignment(assignmentId,grade,user));
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/rate_course")
    public ResponseEntity rateCourse(@RequestParam("course_id") final Long courseId,
                                     @RequestParam("rating") final Integer rating,
                                     @CurrentUser User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        courseService.rate(user, courseId, rating),
                        CourseResponseDTO.class)
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

    @PreAuthorize("hasRole('Student')")
    @PostMapping("/teacherRequest")
    public ResponseEntity teacherRequest(@CurrentUser final User user) {
        return ResponseEntity.ok().body(
                teacherRequestService.save(user)
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/{id}/updatePicture")
    public ResponseEntity changePicture(@PathVariable(name = "id") final Long userId,
                                        @CurrentUser final User user,
                                        @RequestParam(name = "picture") final MultipartFile pictureFile) {
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
