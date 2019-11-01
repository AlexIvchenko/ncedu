package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.exceptions.UserWithGivenIdDoesntExist;
import com.ncedu.nc_edu.dao.UserDao;
import com.ncedu.nc_edu.dao.UserRoleDao;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
        user.setEnabled(true);

        UserRole role = userRoleDao.findByRole("ROLE_USER");
        Set<UserRole> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userDao.save(user);
    }

    @Override
    public List<User> findAllUsers()
    {
        List<User> users = new ArrayList<>();
        userDao.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User updateUserInfo(Long id, String username, Date birthday, User.Gender gender, Long height, Integer weight)
                throws UserWithGivenIdDoesntExist {
        Optional<User> userOpt = userDao.findById(id);
        User user = userOpt.orElseThrow(UserWithGivenIdDoesntExist::new);

        if (!username.equals(""))
            user.setUsername(username);

        if (birthday != null)
            user.setBirthday(birthday);

        if (gender != null)
            user.setGender(gender);

        if (height != null && height > 0)
            user.setHeight(height.intValue());

        if (weight != null && weight > 0)
            user.setWeight(weight);

        return userDao.save(user);
    }
}
