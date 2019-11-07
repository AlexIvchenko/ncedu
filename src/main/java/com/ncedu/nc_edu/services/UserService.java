package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.UserResource;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User registerUser(String email, String password) throws EmailAlreadyExistsException;
    List<User> findAllUsers();

    /**
     *
     * @return User entity or exception
     * @throws EntityDoesNotExistsException if user cannot be found
     */
    User findUserById(UUID id) throws EntityDoesNotExistsException;

    /**
     *
     * @param userResource user dto from request
     * @return User model
     * @throws EntityDoesNotExistsException throws if user with given id cannot be found
     */
    User update(UserResource userResource) throws EntityDoesNotExistsException, EmailAlreadyExistsException;
}
