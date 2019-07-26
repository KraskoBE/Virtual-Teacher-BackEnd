package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;


    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public List findAll() {
        return userService.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity findById(@PathVariable final Long id) {
        return userService.findById(id)
                .map(record -> modelMapper.map(record, UserResponseDTO.class))
                .map(record -> ResponseEntity.ok().body(record))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        userService.deleteById(id);
    }

}
