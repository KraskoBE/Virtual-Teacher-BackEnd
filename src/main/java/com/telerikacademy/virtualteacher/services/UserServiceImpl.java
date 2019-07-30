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
    private final CourseRepository courseRepository;
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
        addRole(user, "Student");

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
        Course course = getCourse(courseId);
        User user = getUser(userId);

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
        if (!author.getId().equals(userId) && !userHasRole(author, "Admin"))
            throw new AccessDeniedException("You have no authority to edit this user's picture");

        User toUpdate = getUser(userId);
        Picture newPicture = pictureService.save(author.getId(), userId, pictureFile);
        toUpdate.setPicture(newPicture);

        return Optional.of(userRepository.save(toUpdate));
    }

    //---EOF interface methods
    private boolean isEmailAvailable(String email) {
        return findAll().stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username);
    }

    private Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Course with id:%d not found", id)));

    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id:%d not found", id)));
    }

    private void addRole(User user, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException(String.format("Role %s not found", roleName)));
        user.getRoles().add(role);
    }

    private boolean userHasRole(User user, String role) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(r -> r.equals(role));
    }
}
