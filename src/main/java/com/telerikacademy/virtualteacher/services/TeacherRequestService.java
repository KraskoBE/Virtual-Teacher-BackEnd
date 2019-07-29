package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.TeacherRequest;
import com.telerikacademy.virtualteacher.models.User;

import java.util.List;
import java.util.Optional;

public interface TeacherRequestService {

    List<TeacherRequest> findAll();

    Optional<TeacherRequest> findById(Long id);

    Optional<TeacherRequest> findByUser(User user);

    void deleteById();

    Optional<TeacherRequest> accept(Long teacherRequestId);
}
