package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole.*;
import com.ncedu.nc_edu.security.CustomUserDetails;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserResource> {
    private Class<UserController> controllerClass;

    public UserAssembler() {
        super(User.class, UserResource.class);
        this.controllerClass = UserController.class;
    }

    @Override
    public UserResource toModel(User entity) {
        UserResource userResource = new UserResource();
        userResource.setId(entity.getId());
        userResource.setUsername(entity.getUsername());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<GrantedAuthority> roles = new HashSet<>((authentication.getAuthorities()));
        CustomUserDetails currentUser;

        if (!roles.contains(new SimpleGrantedAuthority(UserRoles.ANONYMOUS.getString()))) {
            currentUser = (CustomUserDetails) authentication.getPrincipal();
        } else {
            currentUser = new CustomUserDetails(entity);
        }

        userResource.add(linkTo(methodOn(controllerClass).getById(entity.getId())).withSelfRel().withType("GET"));
        userResource.add(linkTo(methodOn(controllerClass).getReceipts(userResource.getId(), null)).withRel("receipts").withType("GET"));
        if (roles.contains(new SimpleGrantedAuthority(UserRoles.MODERATOR.getString()))
                || roles.contains(new SimpleGrantedAuthority(UserRoles.ADMIN.getString()))
                || currentUser.getUser().getId().equals(entity.getId())
        ){
            userResource.add(linkTo(methodOn(controllerClass).getUserFilters(userResource.getId())).withRel("filters").withType("GET"));
            userResource.add(linkTo(methodOn(controllerClass).getUserReviews(userResource.getId())).withRel("reviews").withType("GET"));
            userResource.add(linkTo(methodOn(controllerClass).getInfo(userResource.getId())).withRel("info").withType("GET"));
        }

        return userResource;
    }
}
