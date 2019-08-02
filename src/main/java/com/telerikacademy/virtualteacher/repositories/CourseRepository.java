package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByNameIgnoreCase(String name);

    Page<Course> findAll(Pageable page);

    Page<Course> findBySubmittedTrueOrderByAverageRatingDescTotalVotesDesc(Pageable pageable);

    Page<Course> findAllByTopicAndSubmittedIsTrue(Topic topic, Pageable pageable);
}
