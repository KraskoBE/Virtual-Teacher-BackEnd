package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.services.contracts.ThumbnailService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thumbnails")
@AllArgsConstructor
public class ThumbnailController {

    private final ThumbnailService thumbnailService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "id") final Long courseId) {
        Resource resource = thumbnailService.findByCourseId(courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
