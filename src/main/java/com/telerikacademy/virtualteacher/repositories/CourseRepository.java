package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
