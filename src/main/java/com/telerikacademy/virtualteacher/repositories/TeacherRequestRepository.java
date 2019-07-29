package com.telerikacademy.virtualteacher.repositories;

import com.telerikacademy.virtualteacher.models.TeacherRequest;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRequestRepository extends JpaRepository<TeacherRequest, Long> {

    Optional<TeacherRequest> findByUser(User user);

    void deleteByUser(User user);

}
