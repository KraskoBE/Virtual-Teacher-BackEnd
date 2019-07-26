package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.dtos.request.CourseRequestDTO;
import com.telerikacademy.virtualteacher.exceptions.global.AlreadyExistsException;
import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Course;
import com.telerikacademy.virtualteacher.models.Topic;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.CourseRepository;
import com.telerikacademy.virtualteacher.repositories.TopicRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service("CourseService")
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> save(CourseRequestDTO course, User user) {

        checkIfAlreadyExists(course.getName());

        Course courseToSave = modelMapper.map(course, Course.class);
        courseToSave.setAuthor(user);
        courseToSave.setTopic(findTopicById(course.getTopic()));

        return Optional.of(courseRepository.save(courseToSave));
    }

    @Override
    public void deleteById(Long id) {

    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException(String.format("Topic with id:%d not found", topicId)));
    }

    private void checkIfAlreadyExists(String courseName) {
        if (courseRepository.findByNameIgnoreCase(courseName).isPresent())
            throw new AlreadyExistsException("Course with that name already exists");
    }
}
