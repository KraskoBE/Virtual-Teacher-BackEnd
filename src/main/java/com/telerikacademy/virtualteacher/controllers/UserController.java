package com.telerikacademy.virtualteacher.controllers;

import com.telerikacademy.virtualteacher.dtos.request.UserUpdateRequestDTO;
import com.telerikacademy.virtualteacher.dtos.response.UserResponseDTO;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.security.CurrentUser;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import com.telerikacademy.virtualteacher.validators.PasswordConstraint;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/me")
    @PreAuthorize("hasRole('Student')")
    public ResponseEntity findCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.findById(user.getId()),
                        UserResponseDTO.class
                )
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable final Long id) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.findById(id),
                        UserResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/{id}/updateInfo")
    public ResponseEntity updateInfo(@PathVariable(name = "id") final Long userId,
                                     @CurrentUser final User currentUser,
                                     @Valid @RequestBody final UserUpdateRequestDTO userUpdateRequestDTO) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.updateInfo(userId, userUpdateRequestDTO, currentUser),
                        UserResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/{id}/updatePassword")
    @Validated
    public ResponseEntity updatePassword(@PathVariable(name = "id") final Long userId,
                                         @CurrentUser final User currentUser,
                                         @RequestParam @PasswordConstraint String newPassword) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.updatePassword(userId, newPassword, currentUser),
                        UserResponseDTO.class
                )
        );
    }

    @PreAuthorize("hasRole('Student')")
    @PutMapping("/{id}/updatePicture")
    public ResponseEntity changePicture(@PathVariable(name = "id") final Long userId,
                                        @CurrentUser final User user,
                                        @RequestParam(name = "picture") final MultipartFile pictureFile) {
        return ResponseEntity.ok().body(
                modelMapper.map(
                        userService.updatePicture(userId, user, pictureFile),
                        UserResponseDTO.class)
        );
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        userService.deleteById(id);
    }
}
