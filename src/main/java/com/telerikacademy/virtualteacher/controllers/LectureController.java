package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.LectureResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.LectureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return ResponseEntity.ok().body(
                modelMapper.map(
                        lectureService.findByCourseAndInnerId(user, courseId, lectureInnerId),
                        LectureResponseDTO.class)
        );
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save(@Valid @ModelAttribute LectureRequestDTO lectureRequestDTO,
                               @CurrentUser User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        lectureService.save(lectureRequestDTO, user),
                        LectureResponseDTO.class)
        );

    }
}
