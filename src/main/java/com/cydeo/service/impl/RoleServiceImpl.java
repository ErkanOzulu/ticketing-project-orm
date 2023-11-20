package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> listAllRoles() {

        //bring all the roles
       //Role entity coming from DB
        //But controller ask roleDTO to use for view

        //convert entity to dto- Mapper add into pom.xml modelmapper
        //get roles from db and convert each role to roleDto

       // roleList.stream().map(obj->roleMapper.convertToDTO(obj)); same with below


        return roleRepository.findAll().stream().map(roleMapper::convertToDTO).collect(Collectors.toList());


    }

    @Override
    public RoleDTO findById(Long id) {

        return roleMapper.convertToDTO(roleRepository.findById(id).get());
    }

    @Override
    public void save(RoleDTO dto) {

    }
}
