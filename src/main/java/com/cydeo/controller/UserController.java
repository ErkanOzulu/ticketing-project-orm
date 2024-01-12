package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> getUsers() {

        List<UserDTO> userDTOS = userService.listAllUsers();

        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userDTOS, HttpStatus.OK));
    }

    @GetMapping("/{username}")
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String username) {


        UserDTO userDTO = userService.findByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully reteived", userDTO, HttpStatus.OK));

    }

    @PostMapping()
    @RolesAllowed("Admin")
    public ResponseEntity <ResponseWrapper> createUser(@RequestBody UserDTO user){

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is successfully created",HttpStatus.CREATED));
    }
    @PutMapping()
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper>updateUser(@RequestBody UserDTO user){

        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully updated", user,HttpStatus.OK));

    }
    @DeleteMapping("{username}")
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper>deleteUser(@PathVariable("username") String username){

        userService.deleteByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("User is successfully deleted",HttpStatus.OK));
      //  return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseWrapper("User is successfully deleted",HttpStatus.NO_CONTENT));

        //204 - HttpStatus.NO_CONTENT

    }

}
