package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.UserAssembler;
import com.ncedu.nc_edu.dto.UserDto;
import com.ncedu.nc_edu.exceptions.UserDoesNotExistsException;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.persistence.Access;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private UserService userService;
    private UserAssembler userAssembler;

    public UserController(@Autowired UserService userService, @Autowired UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
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

        UserDto userDto = userAssembler.toModel(user);

        userDto.add(linkTo(methodOn(UserController.class).getUser(userDto.getId())).withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class).updateUser(userDto.getId(), userDto)).withRel("update"));

        return userDto;
    }

    @GetMapping(value = "/users")
    @ResponseBody
    public List<UserDto> showAllUsers() {
        List<User> users = userService.findAllUsers();
        return users.stream()
                .map(userAssembler::toModel)
                .peek(userDto -> {
                    userDto.add(linkTo(methodOn(UserController.class).getUser(userDto.getId())).withSelfRel());
                    userDto.add(linkTo(methodOn(UserController.class).
                            updateUser(userDto.getId(), userDto)).withRel("update"));
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/users/{id}")
    public UserDto getUser(@PathVariable UUID id) {
        User user;

        try {
            user = userService.findUserById(id);
        } catch (UserDoesNotExistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
        }

        UserDto userDto = userAssembler.toModel(user);

        userDto.add(linkTo(methodOn(UserController.class).getUser(userDto.getId())).withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class).updateUser(userDto.getId(), userDto)).withRel("update"));

        return userDto;
    }

    // как менять пароль?-+
    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == authentication.principal.getUser().getId() or hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public UserDto updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserDto userDto)
    {
        userDto.setId(id);

        try {
            UserDto updatedUser = userAssembler.toModel(userService.updateUser(userDto));

            updatedUser.add(linkTo(methodOn(UserController.class).getUser(updatedUser.getId())).withSelfRel());
            updatedUser.add(linkTo(methodOn(UserController.class).updateUser(updatedUser.getId(), updatedUser)).withRel("update"));

            return updatedUser;
        } catch (ParseException ex) {
            switch (ex.getErrorOffset()) {
                case 0:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gender parsing error");
                case 1:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthday parsing error");
                case 2:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Height parsing error");
                case 3:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weight parsing error");
            }
        } catch (EmailAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken");
        } catch (UserDoesNotExistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id");
        }

        // should never be thrown
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error. Try again later");
    }
}