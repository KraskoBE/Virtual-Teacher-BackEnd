package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE})
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public UserController(ModelMapper modelMapper,
                          UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping
    public List findAll() {
        return userService.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity save(@Valid @RequestBody UserRequestDTO user) {
        return userService.save(user)
                .map(record ->
                        ResponseEntity.ok().body(modelMapper.map(record, UserResponseDTO.class)))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use"));
    }

    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(record ->
                        ResponseEntity.ok().body(modelMapper.map(record, UserResponseDTO.class)))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

}
