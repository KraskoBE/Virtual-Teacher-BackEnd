package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.AssignmentService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
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
    private final UserService userService;

    @PostMapping(consumes = {"multipart/form-data"})
    public Assignment save(@RequestParam("file") final MultipartFile file,
                           @RequestParam("lectureId") final Long lectureId,
                           @CurrentUser final User user) {
        return assignmentService.save(user.getId(), lectureId, file);
    }

    @GetMapping("/{lectureId}/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable final Long lectureId,
                                                 @PathVariable final Long userId) {

        Resource resource = assignmentService.findByLectureIdAndUserId(lectureId, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment: filename=\"" + resource.getFilename() +"\"")
                .body(resource);
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity gradeAssignment(@PathVariable("id") Long assignmentId,
                                          @RequestParam("grade") Integer grade,
                                          @CurrentUser User user) {
        return ResponseEntity.ok().body(userService.gradeAssignment(assignmentId,grade,user));
    }
}
