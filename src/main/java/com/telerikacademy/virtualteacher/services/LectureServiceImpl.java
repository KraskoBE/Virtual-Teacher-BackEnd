package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.auth.AccessDeniedException;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.BadRequestException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import com.telerikacademy.virtualteacher.services.contracts.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service("LectureService")
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;

    private final VideoService videoService;
    private final TaskService taskService;
    private final CourseService courseService;
    private final UserService userService;

    @Override
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Lecture findById(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NotFoundException("Lecture not found"));
    }

    @Override
    public List<Lecture> findAllByCourse(Course course) {
        return lectureRepository.findAllByCourse_Id(course.getId());
    }

    @Override
    public Lecture findByCourseAndInnerId(User user, Long courseId, Long lectureId) {
        Course course = courseService.findById(courseId);

        Lecture lecture = course.getLectures().stream()
                .filter(lecture1 -> lecture1.getInnerId().equals(lectureId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Lecture not found"));

        if (!isUserEnrolled(user, course) && !userService.hasRole(user, Role.Name.Admin))
            throw new AccessDeniedException("You are not enrolled for this course");

        if (!hasUserFinishedPrevious(user, course, lecture) && !userService.hasRole(user, Role.Name.Admin))
            throw new AccessDeniedException("You need to finish the previous lecture first");

        return lecture;
    }

    @Override
    public Lecture save(LectureRequestDTO lectureRequestDTO, User author) {
        checkIfAlreadyExists(lectureRequestDTO.getName());

        Course course = courseService.findById(lectureRequestDTO.getCourseId());
        if (course.isSubmitted())
            throw new BadRequestException("Course is already saved");

        Lecture lectureToSave = new Lecture();

        lectureToSave.setName(lectureRequestDTO.getName());
        lectureToSave.setDescription(lectureRequestDTO.getDescription());
        lectureToSave.setInnerId((long) course.getLectures().size() + 1);
        lectureToSave.setAuthor(author);
        lectureToSave.setCourse(course);

        lectureRepository.save(lectureToSave);

        Video video = videoService.save(author.getId(), lectureToSave.getId(), lectureRequestDTO.getVideoFile());
        Task task = taskService.save(author.getId(), lectureToSave.getId(), lectureRequestDTO.getTaskFile());

        lectureToSave.setVideo(video);
        lectureToSave.setTask(task);

        return lectureRepository.save(lectureToSave);
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
                .filter(lecture1 -> lecture1.getInnerId().equals(lecture.getInnerId()-1))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Previous lecture not found"));

        return user.getFinishedLectures().contains(previousLecture);
    }

}
