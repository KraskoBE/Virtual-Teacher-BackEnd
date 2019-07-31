package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findById(Long userId);

    User save(UserRequestDTO user);

    //User update(Long id, User user);

    void deleteById(Long userId);

    User findByEmail(String email);

    Course enrollCourse(Long userId, Long courseId);

    User updatePicture(Long userId, User author, MultipartFile pictureFile);

    void addRole(User user, Role.Name roleName);

    boolean hasRole(User user, Role.Name roleName);
}
