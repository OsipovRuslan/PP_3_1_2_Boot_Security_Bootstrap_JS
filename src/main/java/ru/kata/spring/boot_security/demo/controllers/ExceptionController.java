package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionController {

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<UserErrorResponse> exceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>(
                new UserErrorResponse("User not found"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    private ResponseEntity<UserErrorResponse>  exceptionHandler(UserNotCreatedException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for(FieldError error : errors) {
            stringBuilder
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return new ResponseEntity<>(
                new UserErrorResponse(stringBuilder.toString()),
                HttpStatus.BAD_REQUEST);
    }

}
