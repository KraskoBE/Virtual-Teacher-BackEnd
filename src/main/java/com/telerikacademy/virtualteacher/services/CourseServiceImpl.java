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
import com.telerikacademy.virtualteacher.services.contracts.CourseService;
import com.telerikacademy.virtualteacher.services.contracts.ThumbnailService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@AllArgsConstructor(onConstructor = @__(@Lazy))
@Service("CourseService")
public class CourseServiceImpl implements CourseService {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ThumbnailService thumbnailService;

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final CourseRatingRepository courseRatingRepository;

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> findAllOrderedByIdDesc(Pageable pageable) {
        return courseRepository.findBySubmittedTrueOrderByIdDesc(pageable);
    }

    @Override
    public Page<Course> findAllByOrderedByAverageRating(Pageable pageable) {
        return courseRepository.findBySubmittedTrueOrderByAverageRatingDescTotalVotesDesc(pageable);
    }

    @Override
    public Page<Course> findAllByTopicOrderedByAverageRatingDesc(Long topicId, Pageable pageable) {
        Topic topic = findTopicById(topicId);
        return courseRepository.findByTopicAndSubmittedIsTrueOrderByAverageRatingDesc(topic, pageable);
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
        courseToSave.setThumbnail(null);

        courseRepository.save(courseToSave);

        Thumbnail thumbnail = thumbnailService.save(author.getId(), courseToSave.getId(), course.getThumbnailFile());
        courseToSave.setThumbnail(thumbnail);

        return courseRepository.save(courseToSave);
    }

    @Override
    public Course submit(Long courseId, User user) {
        Course course = findById(courseId);

        if(course.getLectures().size()==0)
            throw new BadRequestException("Add lectures before saving the course");

        if(course.getAuthor().equals(user))
            course.setSubmitted(true);

        return courseRepository.save(course);
    }

    @Override
    public void deleteById(Long courseId) {

    }

    @Override
    public Course rate(User user, Long courseId, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating should be between 1 and 5");
        }

        Course course = findById(courseId);

        if(!user.getFinishedCourses().contains(course))
            throw new BadRequestException("You can not rate a course you haven't finished");

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
