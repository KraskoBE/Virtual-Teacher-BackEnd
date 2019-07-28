package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.Task;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task save(@RequestParam("file") MultipartFile file,
                     @RequestParam("lectureId") Long lectureId,
                     @CurrentUser User user) {
        return taskService.save(user.getId(), lectureId, file);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {

        Resource resource = taskService.findByLectureId(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}