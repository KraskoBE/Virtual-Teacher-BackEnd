package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('Admin')")
public class AdminController {

    private final RoleService roleService;

    @PutMapping("/role")
    public ResponseEntity setRole(@RequestParam("userId") Long userId,
                                  @RequestParam("roleName") Role.Name role,
                                  @CurrentUser User currentUser) {

        return ResponseEntity.ok().body(roleService.setRole(currentUser, userId, role));
    }
}
