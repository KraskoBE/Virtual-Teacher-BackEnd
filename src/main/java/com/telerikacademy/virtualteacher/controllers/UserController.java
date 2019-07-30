package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.TeacherRequestService;
import com.telerikacademy.virtualteacher.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
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
        return userService.findById(id)
                .map(record -> modelMapper.map(record, UserResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @PostMapping("/enroll")
    public ResponseEntity enrollCourse(@RequestParam("courseId") Long courseId,
                                       @RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(userService.enrollCourse(userId, courseId));
    }

    @PostMapping("/teacher_request/{id}")
    public ResponseEntity teacherRequest(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok().body(teacherRequestService.save(id));
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/{id}/updatePicture")
    public ResponseEntity changePicture(@PathVariable(name = "id") Long userId,
                                        @CurrentUser User user,
                                        @RequestParam(name = "picture") MultipartFile pictureFile) {
        return userService.updatePicture(userId, user, pictureFile)
                .map(record -> modelMapper.map(record, UserResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new BadRequestException("Picture could not be updated"));
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        userService.deleteById(id);
    }

}
