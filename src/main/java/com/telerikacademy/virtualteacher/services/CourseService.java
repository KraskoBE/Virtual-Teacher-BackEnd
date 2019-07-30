package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.User;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();

    Optional<Course> findById(Long courseId, User user);

    Optional<Course> save(CourseRequestDTO course, User user);

    //Optional<Course> update(Long id, Course course);

    void deleteById(Long courseId);
}
