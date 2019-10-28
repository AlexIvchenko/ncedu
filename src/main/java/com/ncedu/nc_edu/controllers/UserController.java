package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dao.UserDao;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.services.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Email;

@RestController
public class UserController {
    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(
            @RequestParam String email,
            @RequestParam String password
            ) {

        // TODO validation

        User user;

        try {
            user = userService.registerUser(email, password);
        } catch (EmailAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists", ex);
        }

        return user;
    }
}
