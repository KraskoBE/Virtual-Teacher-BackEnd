package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.TeacherRequest;

import java.util.List;
import java.util.Optional;

public interface TeacherRequestService {

    List<TeacherRequest> findAll();

    Optional<TeacherRequest> findById(Long id);

    Optional<TeacherRequest> findByUserId(Long userId);

    void deleteById(Long id);

    void deleteByUserId(Long userId);

    Optional<TeacherRequest> acceptById(Long Id);

    Optional<TeacherRequest> acceptByUserId(Long userId);
}
