package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserUpdateRequestDTO;
import com.telerikacademy.virtualteacher.models.Assignment;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long userId);

    User save(UserRequestDTO user);

    User updateInfo(Long userId, UserUpdateRequestDTO user, User currentUser);

    User updatePassword(Long userId, String password, User currentUser);

    User updatePicture(Long userId, User author, MultipartFile pictureFile);

    void deleteById(Long userId);

    Course enrollCourse(User user, Long courseId);

    Assignment gradeAssignment(Long assignmentId, Integer grade, User teacher);

    User setRole(Long userId, Role.Name roleName);

    boolean hasRole(User user, Role.Name roleName);
}
