package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionController {

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

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> exceptionHandler(EntityNotFoundException e) {
        return new ResponseEntity<>(
                new UserErrorResponse(e.getMessage()),
                HttpStatus.BAD_REQUEST);

    }

}
