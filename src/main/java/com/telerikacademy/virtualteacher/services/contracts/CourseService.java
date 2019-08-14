package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface CourseService {
    Page<Course> findAll(Pageable pageable);

    List<Course> searchName(String name);

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

}
