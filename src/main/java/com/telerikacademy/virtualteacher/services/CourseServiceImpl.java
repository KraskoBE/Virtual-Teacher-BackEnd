package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.CourseRatingRepository;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.TopicRepository;
import com.telerikacademy.virtualteacher.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service("CourseService")
public class CourseServiceImpl implements CourseService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final CourseRatingRepository courseRatingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @Override
    public Course findByIdAndUser(Long courseId, User user) {
        if (!hasEnrolled(user, courseId) &&
                !userService.hasRole(user, Role.Name.Admin))
            throw new AccessDeniedException("You have no access to this course");

        return findById(courseId);
    }

    @Override
    public Course save(CourseRequestDTO course, User author) {

        checkIfAlreadyExists(course.getName());

        Course courseToSave = modelMapper.map(course, Course.class);
        courseToSave.setAuthor(author);
        courseToSave.setTopic(findTopicById(course.getTopic()));

        return courseRepository.save(courseToSave);
    }

    @Override
    public void deleteById(Long courseId) {

    }

    @Override
    public Optional<Course> rate(Long userId, Long courseId, Integer rating) {
        if (rating < 1 || rating > 5) {
            return Optional.empty();
        }

        Course course = getCourse(courseId);
        User user = getUser(userId);

        Optional<CourseRating> courseRating = courseRatingRepository.findById(new CourseRatingId(userId,courseId));
        courseRatingRepository.save(new CourseRating(user,course,rating));

        course.setAverageRating(course.getCourseRatings().stream()
        .mapToDouble(CourseRating::getRating)
        .average()
        .orElse(0));

        course.setTotalVotes(course.getCourseRatings().size());
        courseRepository.save(course);
        return Optional.of(course);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }


    private Topic findTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException(String.format("Topic with id:%d not found", topicId)));
    }

    private void checkIfAlreadyExists(String courseName) {
        if (courseRepository.findByNameIgnoreCase(courseName).isPresent())
            throw new AlreadyExistsException("Course with that name already exists");
    }

    private boolean hasEnrolled(User user, Long courseId) {
        return user.getEnrolledCourses().stream()
                .map(Course::getId)
                .anyMatch(courseId::equals);
    }
}
