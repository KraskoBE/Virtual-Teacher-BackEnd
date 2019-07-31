package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.Role;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.RoleRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service("UserService")
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseService courseService;
    private final PictureService pictureService;
    private final RoleRepository roleRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> save(UserRequestDTO userRequestDTO) {
        if (!isEmailAvailable(userRequestDTO.getEmail()))
            return Optional.empty();

        User user = modelMapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        addRole(user, Role.Name.Student);

        return Optional.of(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Optional<Course> enrollCourse(Long userId, Long courseId) {
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        User user = findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (course.getUsers().contains(user)) {
            throw new BadRequestException("User is already enrolled to this course");
        } else if (course.getAuthor().equals(user)) {
            throw new BadRequestException("The author cannot enroll its own course");
        } else {
            course.getUsers().add(user);
            return Optional.of(course);
        }
    }

    @Override
    public Optional<User> updatePicture(Long userId, User author, MultipartFile pictureFile) {
        if (!author.getId().equals(userId) && !hasRole(author, Role.Name.Admin))
            throw new AccessDeniedException("You have no authority to edit this user's picture");

        User toUpdate = findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Picture newPicture = pictureService.save(author.getId(), userId, pictureFile);
        toUpdate.setPicture(newPicture);

        return Optional.of(userRepository.save(toUpdate));
    }

    @Override
    public void addRole(User user, Role.Name roleName) {
        if (user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName.toString()::equals))
            return;
        Role role = roleRepository.findByName(roleName.toString())
                .orElseThrow(() -> new NotFoundException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public boolean hasRole(User user, Role.Name roleName) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName.toString()::equals);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username);
    }

    //---EOF interface methods
    private boolean isEmailAvailable(String email) {
        return findAll().stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }
}
