package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.models.TeacherRequest;

import java.util.List;

public interface TeacherRequestService {

    TeacherRequest save(Long userId);

    List<TeacherRequest> findAll();

    TeacherRequest findById(Long id);

    TeacherRequest findByUserId(Long userId);

    void deleteById(Long id);

    void deleteByUserId(Long userId);

    TeacherRequest acceptByUserId(Long userId);
}
