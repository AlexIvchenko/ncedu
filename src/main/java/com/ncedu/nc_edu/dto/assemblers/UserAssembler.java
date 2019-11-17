package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.security.CustomUserDetails;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        userResource.setEmail(entity.getEmail());
        userResource.setUsername(entity.getUsername());
        userResource.setGender(entity.getGender().toString());
        userResource.setBirthday(entity.getBirthday());
        userResource.setHeight(entity.getHeight());
        userResource.setWeight(entity.getWeight());

        Set<GrantedAuthority> roles = new HashSet<>(
                (SecurityContextHolder.getContext().getAuthentication().getAuthorities()));

        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userResource.add(linkTo(methodOn(controllerClass).getById(entity.getId())).withSelfRel());
        if (roles.contains(new SimpleGrantedAuthority("ROLE_MODERATOR"))
                || roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || currentUser.getUser().getId().equals(entity.getId())
        ){
            userResource.add(linkTo(methodOn(controllerClass).update(userResource.getId(), userResource)).withRel("update"));
        }

        return userResource;
    }
}
