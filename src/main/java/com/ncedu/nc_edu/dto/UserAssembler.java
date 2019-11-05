package com.ncedu.nc_edu.dto;

import com.ncedu.nc_edu.models.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserResource> {
    public UserAssembler() {
        super(User.class, UserResource.class);
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

        return userResource;
    }
}
