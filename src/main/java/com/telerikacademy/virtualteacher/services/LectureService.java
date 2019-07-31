package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface LectureService {

    List<Lecture> findAll();

    Optional<Lecture> findById(Long lectureId);

    Optional<List<Lecture>> findAllByCourse(Course course);

    Optional<Lecture> findByCourseAndInnerId(User user, Long courseId, Long lectureId);

    Optional<Lecture> save(LectureRequestDTO lecture, User user);
}
