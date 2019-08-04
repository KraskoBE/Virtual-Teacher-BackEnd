package com.telerikacademy.virtualteacher.services.contracts;

import com.telerikacademy.virtualteacher.models.TeacherRequest;
import com.telerikacademy.virtualteacher.models.User;

import java.util.List;

public interface TeacherRequestService {

    TeacherRequest save(User user);

    List<TeacherRequest> findAll();

    TeacherRequest findById(Long id);

    TeacherRequest findByUserId(Long userId);

    void deleteById(Long id);

    void deleteByUserId(Long userId);

    TeacherRequest acceptByUserId(Long userId);
}
