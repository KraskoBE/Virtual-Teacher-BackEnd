package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.CourseRating;
import com.telerikacademy.virtualteacher.models.CourseRatingId;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRatingRepository extends JpaRepository<CourseRating, CourseRatingId> {
    Optional<CourseRating> findByUserAndCourse(User user, Course course);
}
