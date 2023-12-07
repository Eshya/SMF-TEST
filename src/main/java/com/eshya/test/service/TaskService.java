package com.eshya.test.service;

import com.eshya.test.exception.NotFoundException;
import com.eshya.test.model.Task;
import com.eshya.test.model.UserPortal;
import com.eshya.test.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id " + id));
    }

    public void deleteTaskById(UUID id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getAllTaskByUser(UserPortal userPortal, Pageable pageable) {
        return taskRepository.findAllByUser(userPortal, pageable);
    }
}
