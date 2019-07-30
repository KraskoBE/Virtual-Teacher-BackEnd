package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> save(UserRequestDTO user);

    //Optional<User> update(Long id, User user);

    void deleteById(Long id);

    User findByEmail(String email);

    Optional<Course> enrollCourse(Long userId, Long courseId);

    Optional<User> updatePicture(Long userId, User author, MultipartFile pictureFile);
}
