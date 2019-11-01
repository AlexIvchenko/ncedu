package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.Exceptions.UserWithGivenIdDoesntExists;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.Exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    //url для отладки
    @GetMapping(value = "/users")
    @ResponseBody
    public List<User> showAllUsers() {
        return userService.findAllUsers();
    }

    //Поменяем на Put
    @PostMapping(value = "/users")
    public User updateUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam
            @DateTimeFormat(pattern="yyyy-MM-dd") Date birthday,
            @RequestParam String gender,
            @RequestParam Long height,
            @RequestParam Integer weight
            )
    {
        User.Gender userGender;

        try {
            userGender = User.Gender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            userGender = null;
        }

        try {
            return userService.updateUserInfo(id, username, birthday, userGender, height, weight);
        } catch (UserWithGivenIdDoesntExists e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id");
        }
    }

}
