package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.CourseRatingRepository;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.TopicRepository;
import com.telerikacademy.virtualteacher.services.contracts.ThumbnailService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CourseServiceTests {

    @Mock
    CourseRepository courseRepository;
    @Mock
    TopicRepository topicRepository;
    @Mock
    UserService userService;
    @Mock
    ThumbnailService thumbnailService;
    @Mock
    CourseRatingRepository courseRatingRepository;
    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CourseServiceImpl courseService;


    @Test
    public void findAll_Should_Return_Page() {
        //Arrange
        List<Course> allList = new ArrayList<>();

        Course one = new Course();
        Course two = new Course();

        allList.add(one);
        allList.add(two);

        Page<Course> all = Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(0,2);


        //Act
        when(courseRepository.findAll(Mockito.isA(Pageable.class))).thenReturn(all);

        //Assert
        Assert.assertEquals(courseService.findAll(pageable),all);
    }

    @Test
    public void findAllOrderedByIdDesc_Should_Return_Page() {
        //Arrange
        List<Course> allList = new ArrayList<>();

        Course one = new Course();
        Course two = new Course();

        allList.add(one);
        allList.add(two);

        Page<Course> all = Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(0,2);


        //Act
        when(courseRepository.findBySubmittedTrueOrderByIdDesc(Mockito.isA(Pageable.class))).thenReturn(all);

        //Assert
        Assert.assertEquals(courseService.findAllOrderedByIdDesc(pageable),all);
    }

    @Test
    public void findAllOrderedByAverageRating_Should_Return_Page() {
        //Arrange
        List<Course> allList = new ArrayList<>();

        Course one = new Course();
        Course two = new Course();

        allList.add(one);
        allList.add(two);

        Page<Course> all = Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(0,2);


        //Act
        when(courseRepository.findBySubmittedTrueOrderByAverageRatingDescTotalVotesDesc(Mockito.isA(Pageable.class))).thenReturn(all);

        //Assert
        Assert.assertEquals(courseService.findAllByOrderedByAverageRating(pageable),all);
    }

    @Test
    public void findAllByTopicOrderedByAverageRatingDesc_Should_Return_Page() {
        //Arrange
        List<Course> allList = new ArrayList<>();

        Course one = new Course();
        Course two = new Course();

        allList.add(one);
        allList.add(two);

        Page<Course> all = Mockito.mock(Page.class);
        Pageable pageable = PageRequest.of(0,2);

        Topic topic = new Topic();
        topic.setId(1L);

        //Act
        when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));
        when(courseRepository.findByTopicAndSubmittedIsTrueOrderByAverageRatingDesc(Mockito.isA(Topic.class), Mockito.isA(Pageable.class))).thenReturn(all);

        //Assert
        Assert.assertEquals(courseService.findAllByTopicOrderedByAverageRatingDesc(1L, pageable),all);
    }

    @Test
    public void findById_Should_Return_Course_When_Seccessful() {
        //Arrange
        Course course = new Course();
        course.setId(1L);

        //Act
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        //Assert
        Assert.assertEquals(courseService.findById(course.getId()),course);
    }

    @Test (expected = NotFoundException.class)
    public void findById_Should_ThrowException_When_CourseIsNotFound() {
        //Arrange
        Course course = new Course();
        course.setId(1L);

        //Act & Assert
        Assert.assertEquals(courseService.findById(course.getId()),course);
    }

    @Test
    public void findByIdAndUser_Should_Return_Course_When_Successful() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.getRoles().add(new Role(Role.Name.Student));
        user.getRoles().add(new Role(Role.Name.Teacher));

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(user);

        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        //Act & Assert
        Assert.assertEquals(courseService.findByIdAndUser(course.getId(), user),course);
    }

    @Test (expected = AccessDeniedException.class)
    public void findByIdAndUser_Should_ThrowException_When_InsufficientPermissions() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.getRoles().add(new Role(Role.Name.Student));
        user.getRoles().add(new Role(Role.Name.Teacher));

        User author = new User();
        author.setId(2L);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(author);

        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        //Act & Assert
        Assert.assertEquals(courseService.findByIdAndUser(course.getId(), user),course);
    }

    @Test
    public void save_Should_Return_Course_When_Successful() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.getRoles().add(new Role(Role.Name.Student));
        user.getRoles().add(new Role(Role.Name.Teacher));

        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "video.mp4";
        final String type = "video/mp4";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Thumbnail thumbnail = new Thumbnail(name, type, file.getSize(), name, user);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(user);
        course.setName("Course Name");
        course.setDescription("Course Description that is a bit longer than the name");

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Business");

        CourseRequestDTO courseDTO = new CourseRequestDTO(course.getName(), topic.getId(), course.getDescription(), file);


        when(modelMapper.map(courseDTO, Course.class)).thenReturn(course);
        when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));
        when(thumbnailService.save(user.getId(), course.getId(), courseDTO.getThumbnailFile())).thenReturn(thumbnail);
        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        //Act
        courseService.save(courseDTO, user);

        //Assert
        verify(courseRepository, times(2)).save(course);
    }

    @Test (expected = AlreadyExistsException.class)
    public void save_Should_ThrowException_When_CourseAlreadyExists() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.getRoles().add(new Role(Role.Name.Student));
        user.getRoles().add(new Role(Role.Name.Teacher));

        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "video.mp4";
        final String type = "video/mp4";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Thumbnail thumbnail = new Thumbnail(name, type, file.getSize(), name, user);

        Course course = new Course();
        course.setId(1L);
        course.setAuthor(user);
        course.setName("Course Name");
        course.setDescription("Course Description that is a bit longer than the name");

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Business");

        CourseRequestDTO courseDTO = new CourseRequestDTO(course.getName(), topic.getId(), course.getDescription(), file);

        when(courseRepository.findByNameIgnoreCase(course.getName())).thenThrow(new AlreadyExistsException("Couse with that name already exists"));
        when(modelMapper.map(courseDTO, Course.class)).thenReturn(course);
        when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));
        when(thumbnailService.save(user.getId(), course.getId(), courseDTO.getThumbnailFile())).thenReturn(thumbnail);
        when(userService.hasRole(user, Role.Name.Admin)).thenReturn(false);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        //Act
        courseService.save(courseDTO, user);

        //Assert
        verify(courseRepository, times(2)).save(course);
    }

    @Test (expected = BadRequestException.class)
    public void rate_Should_ThrowException_When_InvalidGradeIsPassed() {
        //Arrange
        User user = new User();
        Course course = new Course();
        course.setId(1L);
        Integer rating = 99;

        //Act
        courseService.rate(user,course.getId(),rating);
    }

    @Test (expected = NotFoundException.class)
    public void rate_Should_ThrowException_When_CourseIsNotFound() {
        //Arrange
        User user = new User();
        Course course = new Course();
        course.setId(1L);
        Integer rating = 4;

        //Act
        courseService.rate(user,course.getId(),rating);
    }

    @Test (expected = BadRequestException.class)
    public void rate_Should_ThrowException_When_CourseIsAlreadyReatedByUser() {
        //Arrange
        User user = new User();
        Course course = new Course();
        course.setId(1L);
        Integer rating = 4;

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(courseRatingRepository.findByUserAndCourse(user,course)).thenReturn(Optional.of(new CourseRating()));

        //Act
        courseService.rate(user,course.getId(),rating);
    }

    @Test
    public void rate_Should_Save_When_Successful() {
        //Arrange
        User user = new User();
        Course course = new Course();
        course.setId(1L);
        Integer rating = 4;
        CourseRating courseRating = new CourseRating();
        courseRating.setRating(4);
        course.setCourseRatings(new HashSet<>());
        course.getCourseRatings().add(courseRating);

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        //Act
        courseService.rate(user,course.getId(),rating);

        //Assert
        verify(courseRepository, times(1)).save(course);
    }

}



























