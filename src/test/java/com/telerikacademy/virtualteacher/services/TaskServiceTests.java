package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Picture;
import com.telerikacademy.virtualteacher.models.Task;
import com.telerikacademy.virtualteacher.repositories.TaskRepository;
import com.telerikacademy.virtualteacher.services.contracts.LectureService;
import com.telerikacademy.virtualteacher.services.contracts.TaskService;
import com.telerikacademy.virtualteacher.services.contracts.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TaskServiceTests {

    @Mock
    TaskRepository taskRepository;
    @Mock
    UserService userService;
    @Mock
    LectureService lectureService;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    public void save_Should_Return_Task_When_Successful() {
        //Arrange
        final Long authorId = 1L;
        final Long lectureId = 1L;

        byte[] content = new byte[20];
        new Random().nextBytes(content);

        final String name = "task.txt";
        final String type = "text/plain";
        MockMultipartFile file = new MockMultipartFile(name, name, type,content);
        Task task  = new Task();

        when(taskRepository.save(Mockito.isA(Task.class))).thenReturn(task);

        //Act
        Task result = taskService.save(authorId, lectureId, file);

        //Assert
        Assert.assertEquals(result, task);
    }

    @Test (expected = NotFoundException.class)
    public void findByLectureId_Should_ThrowException_When_NotFound() {
        //Arrange
        final Long lectureId = 1L;
        Lecture lecture = new Lecture();
        lecture.setId(lectureId);

        when(taskRepository.findByLecture(lecture)).thenReturn(Optional.empty());

        //Act & Assert
        taskService.findByLectureId(lectureId);
    }

    @Test
    public void findByLectureId_Should_Return_Resource_When_Successful() {
        //Arrange
        final String fileName = "FileName";
        final Long lectureId = 1L;
        Lecture lecture = new Lecture();
        lecture.setId(lectureId);

        Task task = new Task();
        task.setFileName(fileName);

        when(taskRepository.findByLecture(lecture)).thenReturn(Optional.of(task));
        Resource resource = taskService.loadFileByName(fileName);

        //Act
        Resource result = taskService.findByLectureId(lectureId);

        //Assert
        Assert.assertEquals(resource,result);
    }
}
















