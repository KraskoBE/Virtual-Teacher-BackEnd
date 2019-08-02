package com.telerikacademy.virtualteacher.services;

import com.telerikacademy.virtualteacher.exceptions.global.NotFoundException;
import com.telerikacademy.virtualteacher.models.Lecture;
import com.telerikacademy.virtualteacher.models.Task;
import com.telerikacademy.virtualteacher.models.User;
import com.telerikacademy.virtualteacher.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service("TaskService")
public class TaskServiceImpl extends StorageServiceBase implements TaskService {

    private final TaskRepository taskRepository;
    private final LectureService lectureService;
    private final UserService userService;

    @Lazy
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           LectureService lectureService) {
        super(
                Paths.get("./uploads/tasks"),
                "http://localhost:8080/api/tasks"
        );

        this.taskRepository = taskRepository;
        this.userService = userService;
        this.lectureService = lectureService;
    }

    @Override
    public void setAllowedTypes() {
        allowedTypes.put("text/plain", "txt");
        allowedTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        allowedTypes.put("application/msword", "doc");
        allowedTypes.put("text/x-java-source,java", "java");
        allowedTypes.put("application/pdf", "pdf");
    }

    //Beginning of interface methods
    @Override
    public Task save(Long authorId, Long lectureId, MultipartFile taskFile) {
        User author = userService.findById(authorId);
        Lecture lecture = lectureService.findById(lectureId);

        String taskType = allowedTypes.get(taskFile.getContentType());
        String taskName = String.format("task_L%d.%s", lectureId, taskType);
        String taskUrl = storeFile(taskFile, lectureId, taskName);

        return taskRepository.save(
                new Task(
                        author,
                        lecture,
                        taskUrl,
                        taskFile.getContentType(),
                        taskFile.getSize(),
                        taskName
                )
        );
    }

    @Override
    public Resource findByLectureId(Long lectureId) {
        Lecture lecture = lectureService.findById(lectureId);

        Task task = taskRepository.findByLecture(lecture)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        String fileName = task.getFileName();

        return loadFileByName(fileName);
    }
}
