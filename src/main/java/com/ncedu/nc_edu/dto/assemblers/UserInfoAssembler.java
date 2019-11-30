package com.ncedu.nc_edu.dto.assemblers;

import com.ncedu.nc_edu.controllers.UserController;
import com.ncedu.nc_edu.dto.resources.UserInfoResource;
import com.ncedu.nc_edu.models.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserInfoAssembler extends RepresentationModelAssemblerSupport<User, UserInfoResource> {
    public UserInfoAssembler() {
        super(User.class, UserInfoResource.class);
    }

    @Override
    public UserInfoResource toModel(User entity) {
        UserInfoResource resource = new UserInfoResource();
        resource.setId(entity.getId());
        resource.setUsername(entity.getUsername());
        resource.setEmail(entity.getEmail());
        resource.setBirthday(entity.getBirthday());
        resource.setGender(entity.getGender().toString());
        resource.setHeight(entity.getHeight());
        resource.setWeight(entity.getWeight());

        resource.add(linkTo(methodOn(UserController.class).getInfo(entity.getId())).withSelfRel().withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).getById(entity.getId())).withRel("user").withType("GET"));
        resource.add(linkTo(methodOn(UserController.class).update(entity.getId(), null)).withRel("update").withType("PUT"));
        return resource;
    }
}
