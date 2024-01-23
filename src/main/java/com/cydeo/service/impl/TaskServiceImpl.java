package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.TaskService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }


    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return taskMapper.convertToDto(task.get());
        }

        // taskMapper.convertToDto(taskRepository.findById(id).get());
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        taskRepository.save(task);

    }

    @Override
    public void update(TaskDTO dto) {

        Optional<Task> task = taskRepository.findById(dto.getId());

        Task convertedTask = taskMapper.convertToEntity(dto);

        if (task.isPresent()) {
            convertedTask.setId(task.get().getId());
            convertedTask.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
        }

        taskRepository.save(convertedTask);

    }

    @Override
    public void delete(Long id) {

        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent()) {

            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }


        //Below is same but if there isn't any it will check, and we can add exception.Optional protect from nullPointerException

//        Task foundTask=taskRepository.findById(id).get();
//        foundTask.setIsDeleted(true);
//        taskRepository.save(foundTask);
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTask(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTask(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        List<TaskDTO> list = listAllByProject(projectDTO);
        list.forEach(taskDTO -> delete(taskDTO.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        List<TaskDTO> list = listAllByProject(projectDTO);
        list.forEach(taskDTO -> {
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);
        });

    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details= (SimpleKeycloakAccount) authentication.getDetails();
        String username=details.getKeycloakSecurityContext().getToken().getPreferredUsername();

        User loggedInUser =userRepository.findByUserNameAndIsDeleted(username,false);
//        User loggedInUser =userRepository.findByUserName("john@employee.com");

        List<Task>list=taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,loggedInUser);
        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(TaskDTO dto) {

        Optional<Task>task=taskRepository.findById(dto.getId());


        if (task.isPresent()){
            task.get().setTaskStatus(dto.getTaskStatus());
            taskRepository.save(task.get());
        }

    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details= (SimpleKeycloakAccount) authentication.getDetails();
        String username=details.getKeycloakSecurityContext().getToken().getPreferredUsername();

        User loggedInUser =userRepository.findByUserNameAndIsDeleted(username,false);
//        User loggedInUser =userRepository.findByUserName("john@employee.com");

        List<Task>list=taskRepository.findAllByTaskStatusAndAssignedEmployee(status,loggedInUser);
        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> readAllByAssignedEmployee(User assignedEmployee) {
       List<Task>list=taskRepository.findAllByAssignedEmployee(assignedEmployee);

       return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }


    private List<TaskDTO> listAllByProject(ProjectDTO projectDTO) {
        List<Task> list = taskRepository.findAllByProject(projectMapper.convertToProject(projectDTO));
        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
