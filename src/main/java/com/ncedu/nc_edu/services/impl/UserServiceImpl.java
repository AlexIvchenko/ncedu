package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.UserResource;
import com.ncedu.nc_edu.exceptions.UserDoesNotExistsException;
import com.ncedu.nc_edu.repositories.UserRepository;
import com.ncedu.nc_edu.repositories.UserRoleRepository;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public UserServiceImpl(
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired UserRepository userRepository,
            @Autowired UserRoleRepository userRoleRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User registerUser(String email, String password) throws EmailAlreadyExistsException {
        password = passwordEncoder.encode(password);

        if (userRepository.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(true);
        user.setId(UUID.randomUUID());
        user.setGender(User.Gender.UNKNOWN);

        UserRole role = userRoleRepository.findByRole("ROLE_USER").orElseThrow(RuntimeException::new);
        Set<UserRole> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers()
    {
        return new ArrayList<>(userRepository.findAll());
    }

    /**
     *
     * @param userResource user dto from request
     * @return User model
     * @throws UserDoesNotExistsException throws if user with given id cannot be found
     * @throws ParseException throws if parsing error has occurred: {@code ex.getErrorOffset()} represents exact
     * element with error: 0 - gender, 1 - birthday, 2 - height, 3 - weight
     */
    @Override
    public User updateUser(UserResource userResource)
            throws UserDoesNotExistsException, ParseException, EmailAlreadyExistsException
    {
        User oldUser = userRepository.findById(userResource.getId()).orElseThrow(UserDoesNotExistsException::new);

        if (userResource.getGender() != null) {
            if (!userResource.getGender().equals(User.Gender.UNKNOWN.toString())) {
                try {
                    User.Gender gender = User.Gender.valueOf(userResource.getGender());
                    oldUser.setGender(User.Gender.valueOf(userResource.getGender()));
                } catch (IllegalArgumentException ex) {
                    throw new ParseException("Gender parsing error", 0);
                }
            }
        }

        if (userResource.getEmail() != null) {
            if (userRepository.findByEmail(userResource.getEmail()) == null) {
                oldUser.setEmail(userResource.getEmail());
            } else {
                throw new EmailAlreadyExistsException();
            }
        }

        if (userResource.getUsername() != null) {
            oldUser.setUsername(userResource.getUsername());
        }

        if (userResource.getBirthday() != null) {
            if (userResource.getBirthday().compareTo(new Date()) < 0) {
                oldUser.setBirthday(userResource.getBirthday());
            } else {
                throw new ParseException("Birthday parsing error", 1);
            }
        }

        if (userResource.getHeight() != null) {
            if (userResource.getHeight() > 0) {
                oldUser.setHeight(userResource.getHeight());
            } else {
                throw new ParseException("Height must be greater than zero", 2);
            }
        }

        if (userResource.getWeight() != null) {
            if (userResource.getWeight() > 0) {
                oldUser.setWeight(userResource.getWeight());
            } else {
                throw new ParseException("Weight must be greater than zero", 3);
            }
        }

        return userRepository.save(oldUser);
    }

    @Override
    public User findUserById(UUID id) throws UserDoesNotExistsException {
        return userRepository.findById(id).orElseThrow(UserDoesNotExistsException::new);
    }
}
