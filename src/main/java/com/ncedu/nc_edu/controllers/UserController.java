package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.assemblers.RecipeAssembler;
import com.ncedu.nc_edu.dto.assemblers.UserAssembler;
import com.ncedu.nc_edu.dto.assemblers.UserInfoAssembler;
import com.ncedu.nc_edu.dto.resources.RecipeResource;
import com.ncedu.nc_edu.dto.resources.UserInfoResource;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserReview;
import com.ncedu.nc_edu.models.UserRole;
import com.ncedu.nc_edu.security.CustomUserDetails;
import com.ncedu.nc_edu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final UserAssembler userAssembler;
    private final UserInfoAssembler userInfoAssembler;
    private final RecipeAssembler recipeAssembler;

    public UserController(@Autowired UserService userService,
                          @Autowired UserAssembler userAssembler,
                          @Autowired UserInfoAssembler userInfoAssembler,
                          @Autowired RecipeAssembler recipeAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userInfoAssembler = userInfoAssembler;
        this.recipeAssembler = recipeAssembler;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RepresentationModel<UserResource> add(
            @RequestParam @NotBlank(message = "cannot be empty") @Email(message = "must be a valid email") String email,
            @RequestParam @NotBlank(message = "cannot be empty") String password)
    {
        User user;
        user = userService.registerUser(email, password);
        UserResource userResource = userAssembler.toModel(user);
        return userResource;
    }

    @GetMapping(value = "/users")
    public CollectionModel<List<UserResource>> getAll() {
        List<User> users = userService.findAllUsers();

        return new CollectionModel<>(Collections.singleton(users.stream()
                .map(userAssembler::toModel)
                .collect(Collectors.toList())));
    }

    @GetMapping(value = "/users/{id}")
    public RepresentationModel<UserResource> getById(@PathVariable UUID id) {
        User user;
        user = userService.findUserById(id);
        return userAssembler.toModel(user);
    }

    @GetMapping(value = "/users/{id}/info")
    public RepresentationModel<UserInfoResource> getInfo(@PathVariable UUID id) { ;
        User user = userService.findUserById(id);
        return userInfoAssembler.toModel(user);
    }

    @GetMapping(value = "/users/{id}/recipes")
    public CollectionModel<List<RecipeResource>> getRecipes(@PathVariable UUID id) {
        CollectionModel<List<RecipeResource>> resource = new CollectionModel<List<RecipeResource>>(
                Collections.singleton(userService.getRecipesById(id).stream()
                        .map(recipeAssembler::toModel)
                        .collect(Collectors.toList())));
        return resource;
    }

    @GetMapping(value = "/users/@me")
    public RepresentationModel<UserResource> getAuthenticatedUser(Authentication auth) {
        return userAssembler.toModel(((CustomUserDetails) auth.getPrincipal()).getUser());
    }

    @GetMapping(value = "/users/@me/roles")
    public Set<String> getRoles(Authentication auth) {
        Set<String> response = new HashSet<>();
        ((CustomUserDetails) auth.getPrincipal()).getUser().getRoles().stream()
                .forEach(role -> response.add(role.getRole()));
        return response;
    }

    @GetMapping(value = "/users/{id}/reviews")
    public List<UserReview> getUserReviews(@PathVariable UUID id) {
        return userService.getReviewsById(id);
    }

    /*
     *  height >= 0, if 0 - delete height info
     *  weight >= 0, if 0 - delete weight info
     *  birthday < current date
     *  gender = UNKNOWN|MALE|FEMALE
     */
    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<UserInfoResource> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserInfoResource userResource)
    {
        log.info(userResource.toString());
        userResource.setId(id);
        return userInfoAssembler.toModel(userService.update(userResource));
    }
}