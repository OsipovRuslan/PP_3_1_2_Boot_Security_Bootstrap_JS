package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api/users")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService,
                           RoleService roleService1) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService1;
    }

    @GetMapping
    public List<UserDto> allUsers() {
        return userService.findAll().stream()
                .map(userService::convertToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public List<RoleDto> allRoles() {
        return roleService.findAll().stream()
                .map(roleService::convertToRoleDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto userById(@PathVariable("id") long id) {
        return userService.convertToUserDto(userService.findById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        User user = userService.convertToUser(userDto);
        user.setId(0);
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder stringBuilder = new StringBuilder();
            for(FieldError error : errors) {
                stringBuilder
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(stringBuilder.toString());
        }

        userService.save(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> editUser(@RequestBody UserDto userDto, BindingResult bindingResult) {
        User user = userService.convertToUser(userDto);
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder stringBuilder = new StringBuilder();
            for(FieldError error : errors) {
                stringBuilder
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(stringBuilder.toString());
        }
        userService.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        try {
            userService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException();
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> exceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>(
                new UserErrorResponse("User not found"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse>  exceptionHandler(UserNotCreatedException e) {
        return new ResponseEntity<>(
                new UserErrorResponse(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}