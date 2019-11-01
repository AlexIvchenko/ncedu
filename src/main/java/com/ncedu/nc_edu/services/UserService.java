package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.exceptions.UserWithGivenIdDoesntExist;
import com.ncedu.nc_edu.models.User;

import java.util.Date;
import java.util.List;

public interface UserService {
    User registerUser(String email, String password) throws EmailAlreadyExistsException;
    List<User> findAllUsers();
    User updateUserInfo(Long id, String username, Date birthday, User.Gender gender, Long height, Integer weight) throws UserWithGivenIdDoesntExist;
}
