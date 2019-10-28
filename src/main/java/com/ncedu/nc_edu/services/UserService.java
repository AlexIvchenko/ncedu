package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.models.User;

public interface UserService {
    User registerUser(String email, String password) throws EmailAlreadyExistsException;
}
