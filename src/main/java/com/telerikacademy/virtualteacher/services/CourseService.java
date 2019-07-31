package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.User;

import java.util.List;

public interface CourseService {
    List<Course> findAll();

    Course findById(Long courseId);

    Course findByIdAndUser(Long courseId, User user);

    Course save(CourseRequestDTO course, User author);

    //Course update(Long id, Course course);

    void deleteById(Long courseId);

}
