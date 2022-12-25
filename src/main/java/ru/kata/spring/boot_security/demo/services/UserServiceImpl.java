package ru.kata.spring.boot_security.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void save(User user) {

        if(user.getPassword().isEmpty())
            user.setPassword(userRepository.getById(user.getId()).getPassword());
        else
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRoles().isEmpty())
            user.setRoles(userRepository.getById(user.getId()).getRoles());
        else {
            Set<Role> roles = user.getRoles().stream().map(role -> roleService.findByRole(role.getRole()).get())
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto convertToUserDto(User user) {
       return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
