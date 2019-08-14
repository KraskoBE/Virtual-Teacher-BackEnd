package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.CourseRating;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CourseService {
    Page<Course> findAll(Pageable pageable);

    Page<Course> findByName(String name, Pageable pageable);

    Page<Course> findAllOrderedByIdDesc(Pageable pageable);

    Page<Course> findAllByOrderedByAverageRating(Pageable pageable);

    Page<Course> findAllByTopicOrderedByAverageRatingDesc(Long topicId, Pageable pageable);

    Course findById(Long courseId);

    Course findByIdAndUser(Long courseId, User user);

    Course save(CourseRequestDTO course, User author);

    Course submit(Long courseId, User user);

    //Course update(Long id, Course course);

    void deleteById(Long courseId);

    Course rate(User user, Long courseId, Integer rating);

    CourseRating findUserRating(User user, Long courseId);

}
