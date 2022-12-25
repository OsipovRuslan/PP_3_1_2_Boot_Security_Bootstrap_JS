package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.models.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class UserDto {

    long id;

    @NotEmpty(message = "Username should not be empty")
    private String username;
    @Email(message = "Email must be correct")
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @NotEmpty(message = "Roles should not be empty")
    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
