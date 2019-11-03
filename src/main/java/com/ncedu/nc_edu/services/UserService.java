package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.UserDto;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.exceptions.NoAccessException;
import com.ncedu.nc_edu.exceptions.UserDoesNotExistsException;
import com.ncedu.nc_edu.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public interface UserService {
    User registerUser(String email, String password) throws EmailAlreadyExistsException;
    List<User> findAllUsers();
    User findUserById(UUID id) throws UserDoesNotExistsException;

    /**
     *
     * @param userDto user dto from request
     * @return User model
     * @throws UserDoesNotExistsException throws if user with given id cannot be found
     * @throws ParseException throws if parsing error has occurred: {@code ex.getErrorOffset()} represents exact
     * element with error: 0 - gender, 1 - birthday, 2 - height, 3 - weight
     */
    User updateUser(UserDto userDto) throws UserDoesNotExistsException,
            ParseException, EmailAlreadyExistsException;
}
