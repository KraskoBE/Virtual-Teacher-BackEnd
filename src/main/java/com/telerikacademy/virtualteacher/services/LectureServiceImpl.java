package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.LectureRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.models.*;
import com.telerikacademy.virtualteacher.repositories.LectureRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("LectureService")
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;

    private final VideoService videoService;
    private final TaskService taskService;

    private final ModelMapper modelMapper;

    @Override
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public Optional<List<Lecture>> findAllByCourse(Course course) {
        List<Lecture> lectures = lectureRepository.findAll()
                .stream()
                .filter( x -> x.getCourse().getId().equals(course.getId()))
                .collect(Collectors.toList());
        if (!lectures.isEmpty()) {
            return Optional.of(lectures);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lecture> findById(Long lectureId){
        return lectureRepository.findById(lectureId);
    }

    @Override
    public Optional<Lecture> save(LectureRequestDTO lecture, User user, MultipartFile videoFile, MultipartFile taskFile) {

        checkIfAlreadyExists(lecture.getName());

        Lecture lectureToSave = modelMapper.map(lecture, Lecture.class);

        Video video = videoService.save(user.getId(), lectureToSave.getId(), videoFile);
        Task task = taskService.save(user.getId(), lectureToSave.getId(), taskFile);
        lectureToSave.setAuthor(user);
        lectureToSave.setVideo(video);
        lectureToSave.setTask(task);

        return Optional.of(lectureToSave);
    }

    private void checkIfAlreadyExists(String lectureName) {
        if (lectureRepository.findByNameIgnoreCase(lectureName).isPresent()) {
            throw new AlreadyExistsException("Lecture with that name already exists");
        }
    }

}
