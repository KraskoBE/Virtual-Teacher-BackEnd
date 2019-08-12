package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.dtos.request.UserUpdateRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.auth.EmailAlreadyUsedException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.AssignmentRepository;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.RoleRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import com.telerikacademy.virtualteacher.services.contracts.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@AllArgsConstructor
@Service("UserService")
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final RoleRepository roleRepository;
    private final AssignmentRepository assignmentRepository;

    private final PictureService pictureService;
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final NotificationService notificationService;
    private final LectureService lectureService;

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
        if (isEmailAlreadyUsed(userRequestDTO.getEmail()))
            throw new EmailAlreadyUsedException("Email already in use");

        User user = modelMapper.map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        userRepository.save(user);

        return setRole(user.getId(), Role.Name.Student);
    }

    @Override
    public User updatePassword(Long userId, String password, User currentUser) {
        User oldUser = findById(userId);

        if (oldUser.equals(currentUser) || hasRole(currentUser, Role.Name.Admin)) {
            oldUser.setPassword(passwordEncoder.encode(password));
            return userRepository.save(oldUser);
        } else
            throw new AccessDeniedException("You cannot edit this user");
    }

    @Override
    public User updateInfo(Long userId, UserUpdateRequestDTO userUpdateRequestDTO, User currentUser) {
        User oldUser = findById(userId);

        if (oldUser.equals(currentUser) || hasRole(currentUser, Role.Name.Admin)) {
            if (!userUpdateRequestDTO.getEmail().equals(oldUser.getEmail()))
                if (isEmailAlreadyUsed(userUpdateRequestDTO.getEmail()))
                    throw new EmailAlreadyUsedException("Email already in use");

            oldUser.setFirstName(userUpdateRequestDTO.getFirstName());
            oldUser.setLastName(userUpdateRequestDTO.getLastName());
            oldUser.setEmail(userUpdateRequestDTO.getEmail());
            return userRepository.save(oldUser);
        } else
            throw new AccessDeniedException("You cannot edit this user");
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public Course enrollCourse(User user, Long courseId) {
        Course course = courseService.findByIdAndUser(courseId, user);

        if (course.getUsers().contains(user)) {
            throw new BadRequestException("User is already enrolled to this course");
        }
        if (course.getAuthor().equals(user)) {
            throw new BadRequestException("The author cannot enroll its own course");
        }
        if (user.getFinishedCourses().contains(course)) {
            throw new BadRequestException("User has already finished that course");
        }
        course.getUsers().add(user);
        return courseRepository.save(course);

    }

    @Override
    public Assignment findUserAssignmentByLecture(User user, Long lectureId) {
        Lecture lecture = lectureService.findById(lectureId);

        return user.getAssignments().stream()
                .filter(assignment -> assignment.getLecture().equals(lecture))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Assignment not found"));
    }

    @Override
    public Assignment gradeAssignment(Long assignmentId, Integer grade, User teacher) {
        Assignment assignment = getAssignment(assignmentId);
        Lecture lecture = assignment.getLecture();
        User student = assignment.getAuthor();

        if (!lecture.getAuthor().equals(teacher)) {
            throw new BadRequestException("You must be the lecture author to grade this!");
        }

        if (assignment.getGrade() != 0) {
            throw new BadRequestException("You have already graded this assignment");
        }

        assignment.setGrade(grade);

        lecture.getUsers().add(student);

        String lectureName = assignment.getLecture().getName();
        String message = String.format("Your assignment for lecture %s has been graded with %d", lectureName, grade);
        notificationService.sendNotification(assignment.getAuthor(), message);

        finishCourseIfLastAssignment(student, assignment);

        return assignmentRepository.save(assignment);
    }

    private void finishCourseIfLastAssignment(User student, Assignment assignment) {
        Course course = assignment.getLecture().getCourse();
        if (assignmentService.isLastAssignment(assignment)) {
            course.getGraduatedUsers().add(student);
            courseRepository.save(course);
            String message = String.format("You have finished course: %s, you can now rate it", course.getName());
            notificationService.sendNotification(student, message);
        }
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
    public User setRole(Long userId, Role.Name roleName) {
        User user = findById(userId);


        Collection<Role> newRoles = new HashSet<>();

        switch (roleName) {
            case Admin:
                newRoles.add(getRole(Role.Name.Admin));
            case Teacher:
                newRoles.add(getRole(Role.Name.Teacher));
            case Student:
                newRoles.add(getRole(Role.Name.Student));
                break;
            default:
                throw new BadRequestException("No such role exists");
        }

        user.setRoles(newRoles);
        return userRepository.save(user);
    }

    private Role getRole(Role.Name role) {
        return roleRepository.findByName(role.name())
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

    @Override
    public boolean hasRole(User user, Role.Name roleName) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName.toString()::equals);
    }


    //---EOF interface methods
    private boolean isEmailAlreadyUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
