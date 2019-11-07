package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.UserAssembler;
import com.ncedu.nc_edu.dto.UserResource;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserAssembler userAssembler;

    public UserController(@Autowired UserService userService, @Autowired UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource add(
            @RequestParam @Email String email,
            @RequestParam @NotBlank String password
            )
    {
        User user;
        user = userService.registerUser(email, password);

        UserResource userResource = userAssembler.toModel(user);

        userResource.add(linkTo(methodOn(UserController.class).getById(userResource.getId())).withSelfRel());
        userResource.add(linkTo(methodOn(UserController.class).update(userResource.getId(), userResource)).withRel("update"));

        return userResource;
    }

    @GetMapping(value = "/users")
    public List<UserResource> getAll() {
        List<User> users = userService.findAllUsers();
        return users.stream()
                .map(userAssembler::toModel)
                .peek(userDto -> {
                    userDto.add(linkTo(methodOn(UserController.class).getById(userDto.getId())).withSelfRel());
                    userDto.add(linkTo(methodOn(UserController.class).
                            update(userDto.getId(), userDto)).withRel("update"));
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/users/{id}")
    public UserResource getById(@PathVariable UUID id) {
        User user;
        user = userService.findUserById(id);

        UserResource userResource = userAssembler.toModel(user);

        userResource.add(linkTo(methodOn(UserController.class).getById(userResource.getId())).withSelfRel());
        userResource.add(linkTo(methodOn(UserController.class).update(userResource.getId(), userResource)).withRel("update"));

        return userResource;
    }

    /*
     *  height >= 0, if 0 - delete height info
     *  weight >= 0, if 0 - delete weight info
     *  birthday < current date
     *  gender = UNKNOWN|MALE|FEMALE
     */
    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == authentication.principal.getUser().getId() or hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public UserResource update(
            @PathVariable UUID id,
            @Valid @RequestBody UserResource userResource)
    {
        userResource.setId(id);

        UserResource updatedUser = userAssembler.toModel(userService.update(userResource));
        log.debug(updatedUser.toString());

        updatedUser.add(linkTo(methodOn(UserController.class).getById(updatedUser.getId())).withSelfRel());
        updatedUser.add(linkTo(methodOn(UserController.class).update(updatedUser.getId(), updatedUser)).withRel("update"));

        return updatedUser;
    }
}