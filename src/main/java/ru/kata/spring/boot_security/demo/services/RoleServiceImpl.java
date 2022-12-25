package ru.kata.spring.boot_security.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<Role> findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role convertToRole(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }

    @Override
    public RoleDto convertToRoleDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }
}
