package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.CourseRating;
import com.telerikacademy.virtualteacher.models.CourseRatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRatingRepository extends JpaRepository<CourseRating, CourseRatingId> {

}
