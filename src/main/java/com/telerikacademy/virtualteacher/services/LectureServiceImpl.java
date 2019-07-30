package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("LectureService")
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;

    private final VideoService videoService;
    private final TaskService taskService;
    private final CourseRepository courseRepository;

    //private final ModelMapper modelMapper;

    @Override
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Optional<List<Lecture>> findAllByCourse(Course course) {
        List<Lecture> lectures = lectureRepository.findAll()
                .stream()
                .filter(x -> x.getCourse().getId().equals(course.getId()))
                .collect(Collectors.toList());
        if (!lectures.isEmpty()) {
            return Optional.of(lectures);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return lectureRepository.findById(lectureId);
    }

    @Override // TODO do with modelMapper without breaking everything
    public Optional<Lecture> save(LectureRequestDTO lectureRequestDTO, User user) {
        checkIfAlreadyExists(lectureRequestDTO.getName());

        Course course = getCourse(lectureRequestDTO.getCourseId());

        Lecture lectureToSave = new Lecture();

        lectureToSave.setName(lectureRequestDTO.getName());
        lectureToSave.setDescription(lectureRequestDTO.getDescription());
        lectureToSave.setInnerId((long) course.getLectures().size() + 1);
        lectureToSave.setAuthor(user);
        lectureToSave.setCourse(course);

        lectureRepository.save(lectureToSave);

        Video video = videoService.save(user.getId(), lectureToSave.getId(), lectureRequestDTO.getVideoFile());
        Task task = taskService.save(user.getId(), lectureToSave.getId(), lectureRequestDTO.getTaskFile());

        lectureToSave.setVideo(video);
        lectureToSave.setTask(task);

        return Optional.of(lectureRepository.save(lectureToSave));
    }

    private void checkIfAlreadyExists(String lectureName) {
        if (lectureRepository.findByNameIgnoreCase(lectureName).isPresent()) {
            throw new AlreadyExistsException("Lecture with that name already exists");
        }
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(String.format("Course with ID:%d not found", courseId)));
    }
}
