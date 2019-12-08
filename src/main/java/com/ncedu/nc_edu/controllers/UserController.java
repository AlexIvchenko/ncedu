package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.dto.UserRegistrationCredentials;
import com.ncedu.nc_edu.dto.assemblers.RecipeAssembler;
import com.ncedu.nc_edu.dto.assemblers.UserAssembler;
import com.ncedu.nc_edu.dto.assemblers.UserReviewAssembler;
import com.ncedu.nc_edu.dto.resources.RecipeResource;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.dto.resources.UserReviewResource;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserReview;
import com.ncedu.nc_edu.security.CustomUserDetails;
import com.ncedu.nc_edu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final UserAssembler userAssembler;
    private final UserReviewAssembler userReviewAssembler;
    private final RecipeAssembler recipeAssembler;

    public UserController(@Autowired UserService userService,
                          @Autowired UserAssembler userAssembler,
                          @Autowired UserReviewAssembler userReviewAssembler,
                          @Autowired RecipeAssembler recipeAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userReviewAssembler = userReviewAssembler;
        this.recipeAssembler = recipeAssembler;
    }

    //    @PostMapping(value = "/register") //
//    public ResponseEntity<RepresentationModel<UserResource>> add(
//            @RequestParam @NotBlank(message = "cannot be empty") @Email(message = "must be a valid email") String email,
//            @RequestParam @NotBlank(message = "cannot be empty") String password)
    @PostMapping(value = "/register")
    public ResponseEntity<RepresentationModel<UserResource>> add(@RequestBody @Valid UserRegistrationCredentials credentials) {
        User user = userService.registerUser(credentials.getEmail(), credentials.getPassword());
        UserResource userResource = userAssembler.toModel(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResource);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<CollectionModel<UserResource>> getAll() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(userAssembler.toCollectionModel(users));
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<RepresentationModel<UserResource>> getById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    @GetMapping(value = "/users/{id}/recipes")
    public ResponseEntity<CollectionModel<RecipeResource>> getRecipes(@PathVariable UUID id) {
        return ResponseEntity.ok(recipeAssembler.toCollectionModel(userService.getRecipesById(id)));
    }

    @GetMapping(value = "/users/@me")
    public ResponseEntity<RepresentationModel<UserResource>> getAuthenticatedUser(Authentication auth) {
        return ResponseEntity.ok(userAssembler.toModel(((CustomUserDetails) auth.getPrincipal()).getUser()));
    }

    @GetMapping(value = "/users/{id}/reviews")
    public ResponseEntity<CollectionModel<UserReviewResource>> getUserReviews(@PathVariable UUID id) {
        return ResponseEntity.ok(userReviewAssembler.toCollectionModel(userService.getReviewsById(id)));
    }

    /*
     *  height >= 0, if 0 - delete height info
     *  weight >= 0, if 0 - delete weight info
     *  birthday < current date
     *  gender = UNKNOWN|MALE|FEMALE
     */
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<RepresentationModel<UserResource>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserResource userResource) {
        log.info(userResource.toString());
        userResource.setId(id);
        return ResponseEntity.ok(userAssembler.toModel(userService.update(userResource)));
    }
}