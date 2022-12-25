package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/user/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDto getPrincipal(Authentication auth) {
        Optional<User> optionalUser = userService.findByUsername(auth.getName());
        if (optionalUser.isEmpty())
            throw new UserNotFoundException();
        return userService.convertToUserDto(optionalUser.get());
    }
}
