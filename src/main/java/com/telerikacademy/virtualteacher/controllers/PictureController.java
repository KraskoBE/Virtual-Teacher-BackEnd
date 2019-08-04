package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.services.contracts.PictureService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pictures")
@AllArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable final Long id) {
        Resource resource = pictureService.findByUserId(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
