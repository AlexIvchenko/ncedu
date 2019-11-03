package com.ncedu.nc_edu.dto;

import com.ncedu.nc_edu.models.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserDto> {
    public UserAssembler() {
        super(User.class, UserDto.class);
    }

    @Override
    public UserDto toModel(User entity) {
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setEmail(entity.getEmail());
        userDto.setUsername(entity.getUsername());
        userDto.setGender(entity.getGender().toString());
        userDto.setBirthday(entity.getBirthday());
        userDto.setHeight(entity.getHeight());
        userDto.setWeight(entity.getWeight());

        return userDto;
    }
}
