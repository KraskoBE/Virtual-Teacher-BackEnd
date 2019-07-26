package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
