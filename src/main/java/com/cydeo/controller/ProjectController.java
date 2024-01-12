package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;

    }
    @RolesAllowed({"Admin","Manager"})
    @GetMapping()
    public ResponseEntity<ResponseWrapper> getProjects() {
        List<ProjectDTO> projectDTOs = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("All projects are Successfully retrieved", projectDTOs, HttpStatus.OK));

    }
    @RolesAllowed("Manager")
    @GetMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("projectCode") String projectCode) {

        ProjectDTO projectDTO = projectService.getByProjectCode(projectCode);

        return ResponseEntity.ok(new ResponseWrapper("Project is successfully retrieved", projectDTO, HttpStatus.OK));

    }
    @RolesAllowed("Manager")
    @PostMapping()
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDto) {

        projectService.save(projectDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Project is successfully created", HttpStatus.CREATED));
    }
    @RolesAllowed("Manager")
    @PutMapping("/update")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDto) {

        projectService.update(projectDto);

        return ResponseEntity.ok(new ResponseWrapper("Project is successfully updated", HttpStatus.OK));


    }

    @RolesAllowed("Manager")
    @DeleteMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode") String projectcode) {
        projectService.delete(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is succesfully deleted", HttpStatus.OK));
    }

    @RolesAllowed("Manager")
    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager() {


        List<ProjectDTO> projects = projectService.listAllProjectDetails();

        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", HttpStatus.OK));
    }
    @RolesAllowed("Manager")
    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String projectCode) {
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully completed", HttpStatus.OK));
    }

}
