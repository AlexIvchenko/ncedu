package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dao.UserDao;
import com.ncedu.nc_edu.dao.UserRoleDao;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole;
import com.ncedu.nc_edu.services.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private UserRoleDao userRoleDao;

    public UserServiceImpl(
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired UserDao userDao,
            @Autowired UserRoleDao userRoleDao
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public User registerUser(String email, String password) throws EmailAlreadyExistsException {
        password = passwordEncoder.encode(password);

        if (userDao.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        UserRole role = userRoleDao.findByRole("ROLE_USER");
        Set<UserRole> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userDao.save(user);
    }
}
