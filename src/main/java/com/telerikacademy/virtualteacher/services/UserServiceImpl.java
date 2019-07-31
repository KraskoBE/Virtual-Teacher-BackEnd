package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.auth.EmailAlreadyUsedException;
import com.telerikacademy.virtualteacher.exceptions.auth.UserNotFoundException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.RoleRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@Service("UserService")
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final PictureService pictureService;
    private final RoleRepository roleRepository;
    private AssignmentRepository assignmentRepository;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User save(UserRequestDTO userRequestDTO) {
        if (!isEmailAvailable(userRequestDTO.getEmail()))
            throw new EmailAlreadyUsedException("Email already in use");

        User user = modelMapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        addRole(user, Role.Name.Student);

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Course enrollCourse(Long userId, Long courseId) {
        Course course = getCourse(courseId);

        User user = findById(userId);

        if (course.getUsers().contains(user)) {
            throw new BadRequestException("User is already enrolled to this course");
        } else if (course.getAuthor().equals(user)) {
            throw new BadRequestException("The author cannot enroll its own course");
        } else {
            course.getUsers().add(user);
            return courseRepository.save(course);
        }
    }

    @Override
    public Assignment gradeAssignment(Long assignmentId, Integer grade, User teacher) {
        Assignment assignment = getAssignment(assignmentId);
        Lecture lecture = assignment.getLecture();

        if (!lecture.getAuthor().equals(teacher)) {
            throw new BadRequestException("You must be the lecture author to grade this!");
        }

        assignment.setGrade(grade);
        return assignmentRepository.save(assignment);
    }

    private Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));
    }


    @Override
    public User updatePicture(Long userId, User author, MultipartFile pictureFile) {
        if (!author.getId().equals(userId) && !hasRole(author, Role.Name.Admin))
            throw new AccessDeniedException("You have no authority to edit this user's picture");

        User toUpdate = findById(userId);

        Picture newPicture = pictureService.save(author.getId(), userId, pictureFile);
        toUpdate.setPicture(newPicture);

        return userRepository.save(toUpdate);
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

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }
}
