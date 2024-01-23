package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

//@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final KeycloakService keycloakService;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, TaskService taskService, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.keycloakService = keycloakService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        return userRepository.findAll(Sort.by("firstName")).stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String userName) {
        User user = userRepository.findByUserNameAndIsDeleted(userName,false);
        if (user == null) throw new NoSuchElementException("User not found");
        return userMapper.convertToDto(user);
    }

    @Override
    public UserDTO save(UserDTO dto) {
        dto.setEnabled(true);
        User savedUser=userRepository.save(userMapper.convertToUser(dto));
        keycloakService.userCreate(dto);

        return userMapper.convertToDto(savedUser);
    }

    @Override
    public UserDTO update(UserDTO dto) {

        //Find current user
        User user = userRepository.findByUserNameAndIsDeleted(dto.getUserName(),false);
        //Map updated user dto to entity object
        User convertedUser = userMapper.convertToUser(dto);
        //set id to converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    @Override
    public void deleteByUserName(String username) {

        //it will delete from db but it is not recommended
        userRepository.deleteByUserName(username);
    }

    @Override
    public void delete(String username) throws TicketingProjectException {
        //It will not delete from db, change flag keep in the db

        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName()+""+user.getId());
            userRepository.save(user);
            keycloakService.delete(username);
        }else {
            throw new TicketingProjectException("User can not be deleted");
        }

    }

    private boolean checkIfUserCanBeDeleted(User user) throws TicketingProjectException {

        if (user==null){
            throw new TicketingProjectException("User not found");
        }
        switch (user.getRole().getDescription()) {

            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.readAllByAssignedManager(user);

                return projectDTOList.size()==0;

            case "Employee":
                List<TaskDTO> taskDTOList = taskService.readAllByAssignedEmployee(user);

                return taskDTOList.size()==0;
            default:
                return true;

        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }
}
