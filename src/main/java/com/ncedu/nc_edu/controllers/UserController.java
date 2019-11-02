package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.UserDto;
import com.ncedu.nc_edu.dto.UserMapper;
import com.ncedu.nc_edu.exceptions.UserDoesNotExistsException;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    public UserController(@Autowired UserService userService, @Autowired UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(
            @RequestParam String email,
            @RequestParam String password
            )
    {
        // TODO validation
        User user;

        try {
            user = userService.registerUser(email, password);
        } catch (EmailAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists", ex);
        }

        return userMapper.toDto(user);
    }

    @GetMapping(value = "/users")
    @ResponseBody
    public List<User> showAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserDto userDto)
    {
        userDto.setId(id);
        User user = userMapper.toEntity(userDto);

        try {
            return userService.updateUser(user);
        } catch (UserDoesNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id");
        }
    }
}
