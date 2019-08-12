package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.UserRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.auth.EmailAlreadyUsedException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.*;
import com.telerikacademy.virtualteacher.services.contracts.AssignmentService;
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import com.telerikacademy.virtualteacher.services.contracts.LectureService;
import com.telerikacademy.virtualteacher.services.contracts.PictureService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    PictureService pictureService;
    @Mock
    CourseService courseService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AssignmentRepository assignmentRepository;
    @Mock
    AssignmentService assignmentService;
    @Mock
    LectureService lectureService;
    @Mock
    LectureRepository lectureRepository;
    @Mock
    NotificationServiceImpl notificationService;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        List<User> users = new ArrayList<>();
        users.add(new User("email1@email.com",
                "password",
                "Petur",
                "Petrov",
                LocalDate.now()));
        users.add(new User("email2@email.com",
                "password",
                "Ivan",
                "Ivanov",
                LocalDate.now()));

        //Act
        when(userRepository.findAll()).thenReturn(users);

        //Assert
        Assert.assertEquals(users, userService.findAll());
    }

    @Test(expected = NotFoundException.class)
    public void findById_Should_ThrowNotFound_When_UserIsNotFound() {
        //Act
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert
        userService.findById(1L);
    }

    @Test
    public void save_Should_SaveUser_When_EmailIsNotAlreadyTaken() {
        //Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@email.com",
                "password123",
                "Ivan",
                "Ivanov",
                LocalDate.now());

        User user = new User(userRequestDTO.getEmail(),
                userRequestDTO.getPassword(),
                userRequestDTO.getFirstName(),
                userRequestDTO.getLastName(),
                userRequestDTO.getBirthDate());
        user.setId(1L);

        Role studentRole = new Role(Role.Name.Student);

        //Act
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(Role.Name.Student.toString())).thenReturn(Optional.of(studentRole));
        userService.save(userRequestDTO);

        //Assert
        verify(userRepository, times(2)).save(user);

    }

    @Test(expected = EmailAlreadyUsedException.class)
    public void save_Should_ThrowException_When_EmailAlreadyInUse() {
        //Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@email.com",
                "password123",
                "Ivan",
                "Ivanov",
                LocalDate.now());

        User user = new User(userRequestDTO.getEmail(),
                userRequestDTO.getPassword(),
                userRequestDTO.getFirstName(),
                userRequestDTO.getLastName(),
                userRequestDTO.getBirthDate());

        //Act
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        //Assert
        userService.save(userRequestDTO);
    }

    @Test(expected = AccessDeniedException.class)
    public void updatePassword_Should_ThrowException_When_InsufficientPermissions() {
        //Arrange
        User toBeEdited = new User("email2@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        toBeEdited.setId(2L);

        User currentUser = new User("email@email.com",
                "password123",
                "Ivan",
                "Ivanov",
                LocalDate.now());
        currentUser.setId(1L);

        //Act
        when(userRepository.findById(2L)).thenReturn(Optional.of(toBeEdited));

        //Assert
        userService.updatePassword(2L,"newPass",currentUser);

    }

    @Test
    public void updatePassword_Should_callRepository_when_UserHasAuthority()
    {
        //Arrange
        User toBeEdited = new User("email2@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        toBeEdited.setId(2L);

        User currentUser = new User("email@email.com",
                "password123",
                "Ivan",
                "Ivanov",
                LocalDate.now());
        currentUser.setId(1L);
        currentUser.getRoles().add(new Role(Role.Name.Admin));

        //Act
        when(userRepository.findById(2L)).thenReturn(Optional.of(toBeEdited));
        userService.updatePassword(2L,"newPassword",currentUser);

        //Assert
        verify(userRepository, times(1)).save(toBeEdited);
    }

    @Test
    public void updatePicture_Should_saveUser_When_Successful() {
        //Arrange
        User toUpdate = new User("email2@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        toUpdate.setId(1L);

        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "picture.jpg";
        final String type = "image/jpeg";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Picture picture = new Picture(name, type, file.getSize(), name, toUpdate);

        //Act
        when(userRepository.findById(1L)).thenReturn(Optional.of(toUpdate));
        when(pictureService.save(1L,1L, file)).thenReturn(picture);
        userService.updatePicture(1L, toUpdate ,file);

        //Assert
        verify(userRepository, times(1)).save(toUpdate);
    }

    @Test (expected = AccessDeniedException.class)
    public void updatePicture_Should_ThrowException_When_InsufficientPermissions() {
        //Arrange
        User toUpdate = new User("email2@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        toUpdate.setId(1L);

        User author = new User("email3@email.com",
                "password",
                "Author",
                "Peshov",
                LocalDate.now());
        author.setId(2L);

        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "picture.jpg";
        final String type = "image/jpeg";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Picture picture = new Picture(name, type, file.getSize(), name, toUpdate);

        //Act
        when(userRepository.findById(1L)).thenReturn(Optional.of(toUpdate));
        when(pictureService.save(1L,1L, file)).thenReturn(picture);
        userService.updatePicture(1L, author ,file);

        //Assert
        verify(userRepository, times(1)).save(toUpdate);
    }

    @Test
    public void enrollCourse_Should_ReturnCourse_When_Successful() {
        //Arrange
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        User student = new User("student@email.com",
                "password",
                "Student",
                "Peshov",
                LocalDate.now());
        student.setId(2L);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(author);

        //Act
        when(courseService.findByIdAndUser(1L, student)).thenReturn(course);
        userService.enrollCourse(student, 1L);

        //Assert
        verify(courseRepository,times(1)).save(course);
    }

    @Test (expected = BadRequestException.class)
    public void enrollCourse_Should_ThrowException_When_UserIsAlreadyEnrolled() {
        //Arrange
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        User student = new User("student@email.com",
                "password",
                "Student",
                "Peshov",
                LocalDate.now());
        student.setId(2L);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(author);
        course.getUsers().add(student);

        //Act
        when(courseService.findByIdAndUser(1L, student)).thenReturn(course);
        userService.enrollCourse(student, 1L);

        //Assert
        verify(courseRepository,times(0)).save(course);
    }

    @Test (expected = BadRequestException.class)
    public void enrollCourse_Should_ThrowException_When_UserIsTheAuthor() {
        //Arrange
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(author);

        //Act
        when(courseService.findByIdAndUser(1L, author)).thenReturn(course);
        userService.enrollCourse(author, 1L);

        //Assert
        verify(courseRepository,times(0)).save(course);
    }

    @Test (expected = BadRequestException.class)
    public void enrollCourse_Should_ThrowException_When_UserHasFinishedTheCourse() {
        //Arrange
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        User student = new User("student@email.com",
                "password",
                "Student",
                "Peshov",
                LocalDate.now());
        student.setId(2L);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(author);
        student.getFinishedCourses().add(course);
        //Act
        when(courseService.findByIdAndUser(1L, student)).thenReturn(course);
        userService.enrollCourse(student, 1L);

        //Assert
        verify(courseRepository,times(0)).save(course);
    }

    @Test
    public void gradeAssignment_Should_SaveAssignment_When_Successful() {
        //Arrange
        Integer grade = 5;
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        User student = new User("student@email.com",
                "password",
                "Student",
                "Peshov",
                LocalDate.now());
        student.setId(2L);

        Lecture lecture = new Lecture();
        lecture.setAuthor(author);
        lecture.setName("Test Lecture");

        Assignment assignment = new Assignment();
        assignment.setLecture(lecture);

        //Act
        when(assignmentService.isLastAssignment(assignment)).thenReturn(false);
        when(assignmentRepository.findById(assignment.getId())).thenReturn(Optional.of(assignment));
        userService.gradeAssignment(assignment.getId(),grade, author);

        //Assert
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test (expected = BadRequestException.class)
    public void gradeAssignment_Should_ThrowException_When_UserIsNotAuthor() {
        //Arrange
        Integer grade = 5;
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        User student = new User("student@email.com",
                "password",
                "Student",
                "Peshov",
                LocalDate.now());
        student.setId(2L);

        Lecture lecture = new Lecture();
        lecture.setAuthor(author);
        lecture.setName("Test Lecture");

        Assignment assignment = new Assignment();
        assignment.setLecture(lecture);

        //Act
        when(assignmentService.isLastAssignment(assignment)).thenReturn(false);
        when(assignmentRepository.findById(assignment.getId())).thenReturn(Optional.of(assignment));
        userService.gradeAssignment(assignment.getId(),grade, student);

        //Assert
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test (expected = BadRequestException.class)
    public void gradeAssignment_Should_ThrowException_When_AssignmentAlreadyGraded() {
        //Arrange
        Integer grade = 5;
        User author = new User("author@email.com",
                "password",
                "Pesho",
                "Peshov",
                LocalDate.now());
        author.setId(1L);

        User student = new User("student@email.com",
                "password",
                "Student",
                "Peshov",
                LocalDate.now());
        student.setId(2L);

        Lecture lecture = new Lecture();
        lecture.setAuthor(author);
        lecture.setName("Test Lecture");

        Assignment assignment = new Assignment();
        assignment.setLecture(lecture);
        assignment.setGrade(5);

        //Act
        when(assignmentService.isLastAssignment(assignment)).thenReturn(false);
        when(assignmentRepository.findById(assignment.getId())).thenReturn(Optional.of(assignment));
        userService.gradeAssignment(assignment.getId(),grade, student);

        //Assert
        verify(assignmentRepository, times(1)).save(assignment);
    }

}
