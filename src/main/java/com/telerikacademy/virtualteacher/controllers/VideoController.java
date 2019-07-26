package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.models.Video;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
@AllArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    public Video save(@RequestParam("file") MultipartFile file,
                      @RequestParam("lectureId") Long lectureId,
                      @CurrentUser User user) {
        return videoService.save(user.getId(), lectureId, file);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable(name = "id") Long videoId) {
        return videoService.findByLectureId(videoId)
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new NotFoundException("Video not found"));
    }
}
