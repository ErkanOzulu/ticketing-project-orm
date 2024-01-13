package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.repository.UserRepository;

import java.util.List;

public interface UserService {

List<UserDTO>listAllUsers();
UserDTO findByUserName(String userName);
void save(UserDTO dto);
UserDTO update(UserDTO dto);
void deleteByUserName(String username);
void delete(String username) throws TicketingProjectException;
List<UserDTO>listAllByRole(String role);


}
