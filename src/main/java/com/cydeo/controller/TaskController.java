package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.enums.Status;

import com.cydeo.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("api/v1/task")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;

    }
    @RolesAllowed("Manager")
    @GetMapping()
    public ResponseEntity<ResponseWrapper> getTasks() {

        List<TaskDTO> list = taskService.listAllTasks();

        return ResponseEntity.ok(new ResponseWrapper("All tasks are Successfully retrieved", list, HttpStatus.OK));
    }
    @RolesAllowed("Manager")
    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("taskId") Long taskId) {

        TaskDTO taskDTO = taskService.findById(taskId);

        return ResponseEntity.ok(new ResponseWrapper("Task is successfully retrieved", taskDTO, HttpStatus.OK));

    }
    @RolesAllowed("Manager")
    @PostMapping()
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDto) {

        taskService.save(taskDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Task is successfully created", HttpStatus.CREATED));
    }

    @RolesAllowed("Manager")
    @DeleteMapping("{taskId}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully deleted", HttpStatus.OK));
    }
    @RolesAllowed("Manager")
    @PutMapping()
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);

        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", taskDTO, HttpStatus.OK));

    }
    @RolesAllowed("Employee")
    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() {
        List<TaskDTO> list = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);

        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", list, HttpStatus.OK));
    }

    @RolesAllowed("Employee")
    @PutMapping("/employee/update/{id}")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@RequestBody TaskDTO taskDTO) {


        taskService.updateStatus(taskDTO);

        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully updated", taskDTO, HttpStatus.OK));

    }
    @RolesAllowed("Employee")
    @GetMapping("/employee/archive")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks() {
        List<TaskDTO> taskDTOs = taskService.listAllTasksByStatus(Status.COMPLETE);

        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", taskDTOs, HttpStatus.OK));
    }

}
