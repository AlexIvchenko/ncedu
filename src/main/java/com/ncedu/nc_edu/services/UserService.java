package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.exceptions.AlreadyExistsException;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.models.Filter;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserFilter;
import com.ncedu.nc_edu.models.UserReview;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User registerUser(String email, String password) throws AlreadyExistsException;
    List<User> findAllUsers();

    List<Filter> getUserFiltersById(UUID id);

    List<UserReview> getReviewsById(UUID id);

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
    User update(UserResource userResource) throws EntityDoesNotExistsException, AlreadyExistsException;
}
