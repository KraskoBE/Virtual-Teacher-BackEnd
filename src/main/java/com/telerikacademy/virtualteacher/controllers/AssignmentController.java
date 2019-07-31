package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.AssignmentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/assignments")
@AllArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping(consumes = {"multipart/form-data"})
    public Assignment save(@RequestParam("file") MultipartFile file,
                           @RequestParam("lectureId") Long lectureId,
                           @CurrentUser User user) {
        return assignmentService.save(user.getId(), lectureId, file);
    }

    @GetMapping("/{lectureId}/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long lectureId,
                                                 @PathVariable Long userId) {

        Resource resource = assignmentService.findByLectureIdAndUserId(lectureId, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment: filename=\"" + resource.getFilename() +"\"")
                .body(resource);
    }
}
