package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;

import java.util.List;

public interface LectureService {

    List<Lecture> findAll();

    Lecture findById(Long lectureId);

    List<Lecture> findAllByCourse(Course course);

    Lecture findByCourseAndInnerId(User user, Long courseId, Long lectureId);

    Lecture save(LectureRequestDTO lecture, User author);
}
