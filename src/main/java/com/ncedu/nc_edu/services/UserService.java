package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.exceptions.UserDoesNotExistsException;
import com.ncedu.nc_edu.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public interface UserService {
    User registerUser(String email, String password) throws EmailAlreadyExistsException;
    List<User> findAllUsers();
    User findUserById(UUID id) throws UserDoesNotExistsException;
    User updateUser(User user) throws UserDoesNotExistsException;
}
