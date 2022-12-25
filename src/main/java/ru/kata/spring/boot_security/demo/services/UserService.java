package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    void save(User user);

    List<User> findAll();

    Optional<User> findById(long id);

    void delete(long id);

    UserDto convertToUserDto(User user);
    User convertToUser(UserDto userDto);
}
