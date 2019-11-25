package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserResource> {
    private Class<UserController> controllerClass;
    private SecurityUtils securityUtils;

    public UserAssembler(@Autowired SecurityUtils securityUtils) {
        super(User.class, UserResource.class);
        this.controllerClass = UserController.class;
        this.securityUtils = securityUtils;
    }

    @Override
    public UserResource toModel(User entity) {
        UserResource userResource = new UserResource();
        userResource.setId(entity.getId());
        userResource.setUsername(entity.getUsername());
        userResource.add(linkTo(methodOn(controllerClass).getById(entity.getId())).withSelfRel().withType("GET"));
        userResource.add(linkTo(methodOn(controllerClass).getReceipts(userResource.getId())).withRel("receipts").withType("GET"));

        if (securityUtils.isSelfOrGranted(entity.getId())) {
            userResource.add(linkTo(methodOn(controllerClass).getUserReviews(userResource.getId())).withRel("reviews").withType("GET"));
            userResource.add(linkTo(methodOn(controllerClass).getInfo(userResource.getId())).withRel("info").withType("GET"));
        }

        return userResource;
    }
}
