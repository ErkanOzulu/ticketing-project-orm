package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Project convertToProject(ProjectDTO projectDTO){

        return modelMapper.map(projectDTO,Project.class);
    }

    public ProjectDTO convertToProjectDTO(Project project){
        return modelMapper.map(project,ProjectDTO.class);
    }
}
