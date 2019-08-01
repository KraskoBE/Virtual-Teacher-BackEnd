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
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service("CourseService")
public class CourseServiceImpl implements CourseService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final CourseRatingRepository courseRatingRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<Course> findByOrderedByAverageRating(Pageable pageable) {
        return courseRepository.findBySubmittedTrueOrderByAverageRatingDescTotalVotesDesc(pageable);
    }

    @Override
    public Page<Course> findAllByTopic(Long topicId, Pageable pageable) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found"));
        return courseRepository.findAllByTopicAndSubmittedIsTrue(topic, pageable);
    }

    @Override
    public Course findById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @Override
    public Course findByIdAndUser(Long courseId, User user) {
        Course course = findById(courseId);

        if (userService.hasRole(user, Role.Name.Admin) ||
                course.getAuthor().equals(user) ||
                course.isSubmitted()
        )
            return course;

        throw new AccessDeniedException("You have no access to this course");
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
    public Course rate(User user, Long courseId, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating should be between 1 and 5");
        }
        Course course = getCourse(courseId);

        if (courseRatingRepository.findByUserAndCourse(user, course).isPresent())
            throw new BadRequestException("You have already rated this course");

        courseRatingRepository.save(new CourseRating(user, course, rating));

        course.setAverageRating(course.getCourseRatings().stream()
                .mapToDouble(CourseRating::getRating)
                .average()
                .orElse(0));

        course.setTotalVotes(course.getCourseRatings().size());
        return courseRepository.save(course);
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
