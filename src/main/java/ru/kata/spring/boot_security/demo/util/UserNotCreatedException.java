package ru.kata.spring.boot_security.demo.util;

import org.springframework.validation.BindingResult;

public class UserNotCreatedException extends RuntimeException{
    private BindingResult bindingResult;

    public UserNotCreatedException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
