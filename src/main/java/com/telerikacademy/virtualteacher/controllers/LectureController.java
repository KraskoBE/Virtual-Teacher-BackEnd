package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.LectureResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.LectureService;
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
    public ResponseEntity findByCourseAndId(@CurrentUser final User user,
                                            @PathVariable final Long courseId,
                                            @PathVariable final Long lectureInnerId) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        lectureService.findByCourseAndInnerId(user, courseId, lectureInnerId),
                        LectureResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Teacher')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save(@Valid @ModelAttribute final LectureRequestDTO lectureRequestDTO,
                               @CurrentUser final User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        lectureService.save(lectureRequestDTO, user),
                        LectureResponseDTO.class)
        );

    }
}
