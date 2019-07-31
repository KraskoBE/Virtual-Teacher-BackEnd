package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.LectureResponseDTO;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.CourseService;
import com.telerikacademy.virtualteacher.services.LectureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/lectures")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('Student')")
    @GetMapping("/{courseId}/{lectureInnerId}")
    public ResponseEntity findByCourseAndId(@CurrentUser User user,
                                            @PathVariable Long courseId,
                                            @PathVariable Long lectureInnerId) {
        return lectureService.findByCourseAndInnerId(user, courseId, lectureInnerId)
                .map(record -> modelMapper.map(record, LectureResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new NotFoundException("Lecture not found"));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save(@Valid @ModelAttribute LectureRequestDTO lectureRequestDTO,
                               @CurrentUser User user) {

        return lectureService.save(lectureRequestDTO, user)
                .map(record -> modelMapper.map(record, LectureResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.badRequest().build());

    }
}
