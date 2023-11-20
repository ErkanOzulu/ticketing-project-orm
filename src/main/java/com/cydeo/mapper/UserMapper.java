package com.cydeo.mapper;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //convertToEntity

    public User convertToUser(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    //convertToDTO

    public UserDTO convertToDto(User entity){

        return modelMapper.map(entity,UserDTO.class);
    }


}
