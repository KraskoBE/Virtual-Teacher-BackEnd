package com.telerikacademy.virtualteacher.controllers;


import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.AssignmentService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/assignments")
@AllArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final UserService userService;

    @PreAuthorize("hasRole('Student')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity save(@RequestParam("file") final MultipartFile file,
                               @RequestParam("lectureId") final Long lectureId,
                               @CurrentUser final User user) {
        return ResponseEntity.ok().body(assignmentService.save(user.getId(), lectureId, file));
    }

    @PreAuthorize("hasRole('Student')")
    @GetMapping
    public ResponseEntity findByLecture(@RequestParam final Long lectureId,
                                        @CurrentUser final User user) {
        return ResponseEntity.ok().body(userService.findUserAssignmentByLecture(user, lectureId));
    }

    @GetMapping("/{name}")
    @ResponseBody
    public ResponseEntity findByName(@PathVariable final String name) {
        Resource resource = assignmentService.findByFileName(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment: filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasRole('Teacher')")
    @GetMapping("/{authorId}/teacher")
    public ResponseEntity findAllByLecture(@PathVariable final Long authorId) {
        return ResponseEntity.ok().body(assignmentService.findByCourseAuthor(authorId, 0));
    }


    @PutMapping("/{id}/grade")
    public ResponseEntity gradeAssignment(@PathVariable("id") final Long assignmentId,
                                          @RequestParam("grade") final Integer grade,
                                          @CurrentUser final User user) {
        return ResponseEntity.ok().body(userService.gradeAssignment(assignmentId, grade, user));
    }
}
