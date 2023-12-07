package com.eshya.test.controller;

import com.eshya.test.exception.ResourceNotFoundException;
import com.eshya.test.model.Task;
import com.eshya.test.model.UserPortal;
import com.eshya.test.payload.DefaultMessage;
import com.eshya.test.payload.task.TaskReq;
import com.eshya.test.payload.task.TaskUpdateReq;
import com.eshya.test.service.TaskService;
import com.eshya.test.service.UserPortalService;
import com.eshya.test.utils.Constant;
import com.eshya.test.utils.JwtUtils;
import com.eshya.test.utils.TimeTraker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/"+ Constant.API_NAME +"/task")
public class TaskController {

    private final TaskService taskService;
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final UserPortalService userPortalSrv;
    private final JwtUtils jwtUtils;
    @Autowired
    public TaskController(TaskService taskService, UserPortalService userPortalSrv, JwtUtils jwtUtils) {
        this.taskService = taskService;
        this.userPortalSrv = userPortalSrv;
        this.jwtUtils = jwtUtils;
    }

    @TimeTraker
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskReq taskReq, @RequestHeader("Authorization") String token){

        String username = jwtUtils.getUsernameFromToken(token);
        logger.info("creating task for user : {}", username);
        UserPortal userPortal = userPortalSrv.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Username or password is wrong!", username), "Username or password",
                        username));
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task();
        task.setName(taskReq.getName());
        task.setDescription(taskReq.getDescription());
        task.setCreatedAt(now);
        task.setCompleted(false);
        task.setUser(userPortal);

        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @TimeTraker
    @GetMapping
    public ResponseEntity<List<Task>> getTask(@RequestHeader("Authorization") String token,
                                              @RequestParam(value = "page",required = true) int page,
                                              @RequestParam(value = "size",required = true) int size){
        String username = jwtUtils.getUsernameFromToken(token);
        logger.info("creating task for user : {}", username);
        UserPortal userPortal = userPortalSrv.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Username or password is wrong!", username), "Username or password",
                        username));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Task> tasks = taskService.getAllTaskByUser(userPortal, pageable);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @TimeTraker
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") UUID id, @RequestHeader("Authorization") String token){
        String username = jwtUtils.getUsernameFromToken(token);
        logger.info("creating task for user : {}", username);

        Task task = taskService.getTaskById(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @TimeTraker
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTaskById(@PathVariable("id") UUID id, @RequestBody TaskUpdateReq taskReq, @RequestHeader("Authorization") String token){
        String username = jwtUtils.getUsernameFromToken(token);
        logger.info("update task for user : {}", username);

        Task task = taskService.getTaskById(id);
        if(task == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(taskReq.getName() != null && !taskReq.getName().isEmpty()){
            task.setName(taskReq.getName());
        }
        if(taskReq.getDescription() != null && !taskReq.getDescription().isEmpty()){
            task.setDescription(taskReq.getDescription());
        }

        task.setCompleted(taskReq.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());
        logger.info("task name: {} , description: {} , completed: {}", task.getName(), task.getDescription(), task.isCompleted());
        Task updatedTask = taskService.createTask(task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaskById(@PathVariable("id") UUID id, @RequestHeader("Authorization") String token){
        String username = jwtUtils.getUsernameFromToken(token);
        logger.info("delete task for user : {}", username);

        Task task = taskService.getTaskById(id);
        if(task == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taskService.deleteTaskById(id);
        return new ResponseEntity<>(new DefaultMessage("Task is deleted successfully"), HttpStatus.OK);
    }


}
