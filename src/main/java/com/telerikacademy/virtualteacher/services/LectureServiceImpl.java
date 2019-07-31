package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
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
    private final CourseService courseService;

    //private final ModelMapper modelMapper;

    @Override
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return lectureRepository.findById(lectureId);
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
    public Optional<Lecture> findByCourseAndInnerId(User user, Long courseId, Long lectureId) {
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        Lecture lecture = course.getLectures().stream()
                .filter(lecture1 -> lecture1.getInnerId().equals(lectureId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lecture not found"));

        if (!isUserEnrolled(user, course) && !userHasRole(user, "Admin"))
            throw new AccessDeniedException("You are not enrolled for this course");

        if (!hasUserFinishedPrevious(user, course, lecture) && !userHasRole(user, "Admin"))
            throw new AccessDeniedException("You need to finish the previous lecture first");

        return Optional.of(lecture);
    }

    @Override // TODO do with modelMapper without breaking everything
    public Optional<Lecture> save(LectureRequestDTO lectureRequestDTO, User user) {
        checkIfAlreadyExists(lectureRequestDTO.getName());

        Course course = courseService.findById(lectureRequestDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

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

    private boolean isUserEnrolled(User user, Course course) {
        return course.getUsers().contains(user);
    }

    private boolean hasUserFinishedPrevious(User user, Course course, Lecture lecture) {
        if (lecture.getInnerId() == 1)
            return true;

        Lecture previousLecture = course.getLectures().stream()
                .filter(lecture1 -> lecture.getInnerId().equals(lecture1.getInnerId() - 1))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Previous lecture not found"));
        return user.getFinishedLectures().contains(previousLecture);
    }

    private boolean userHasRole(User user, String roleName) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName::equals);
    }
}
