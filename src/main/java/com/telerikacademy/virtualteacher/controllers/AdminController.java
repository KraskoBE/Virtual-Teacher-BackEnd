package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('Admin')")
public class AdminController {

    private final UserService userService;

    @PutMapping("/role")
    public ResponseEntity setRole(@RequestParam("userId") final Long userId,
                                  @RequestParam("roleName") final Role.Name role) {
        return ResponseEntity.ok().body(userService.setRole(userId, role));
    }
}
