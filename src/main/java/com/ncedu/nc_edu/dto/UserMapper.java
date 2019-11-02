package com.ncedu.nc_edu.dto;

import com.ncedu.nc_edu.exceptions.UserDoesNotExistsException;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole;
import com.ncedu.nc_edu.services.UserRoleService;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserMapper {
    private UserRoleService userRoleService;
    private UserService userService;

    public UserMapper(@Autowired UserRoleService userRoleService, @Autowired UserService userService) {
        this.userRoleService = userRoleService;
        this.userService = userService;
    }

    public User toEntity(UserDto userDto) throws ParseException {
        User user = new User();
        User oldUser;

        try {
            oldUser = userService.findUserById(userDto.getId());
        } catch (UserDoesNotExistsException ex) {
            throw new ParseException(0, "User with given id does not exists");
        }

        user.setPassword(oldUser.getPassword());
        user.setEnabled(oldUser.isEnabled());

        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setBirthday(userDto.getBirthday());
        user.setUsername(userDto.getUsername());
        user.setHeight(userDto.getHeight());
        user.setWeight(userDto.getWeight());

        try {
            User.Gender gender = User.Gender.valueOf(userDto.getGender());
            user.setGender(gender);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(0, "Incorrect user gender");
        }

        Set<UserRole> roles = userRoleService.findRolesByUserId(userDto.getId());
        user.setRoles(roles);

        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        System.out.println(user);
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setGender(user.getGender().toString());
        userDto.setBirthday(user.getBirthday());
        userDto.setHeight(user.getHeight());
        userDto.setWeight(user.getWeight());

        return userDto;
    }
}
