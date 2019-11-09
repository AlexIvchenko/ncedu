package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.UserResource;
import com.ncedu.nc_edu.exceptions.EmailAlreadyExistsException;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole;
import com.ncedu.nc_edu.repositories.UserRepository;
import com.ncedu.nc_edu.repositories.UserRoleRepository;
import com.ncedu.nc_edu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

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

        if (userRepository.findByEmail(email).isPresent()) {
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
     * {@inheritDoc }
     */
    @Override
    public User update(UserResource userResource)
            throws EntityDoesNotExistsException, EmailAlreadyExistsException
    {
        User oldUser = userRepository.findById(userResource.getId()).orElseThrow(() -> new EntityDoesNotExistsException("User"));

        if (userResource.getEmail() != null) {
            userRepository.findByEmail(userResource.getEmail()).orElseThrow(EmailAlreadyExistsException::new);
            oldUser.setEmail(userResource.getEmail());
        }

        if (userResource.getUsername() != null) {
            oldUser.setUsername(userResource.getUsername());
        }

        if (userResource.getGender() != null) {
            oldUser.setGender(User.Gender.valueOf(userResource.getGender()));
        }

        if (userResource.getBirthday() != null) {
            oldUser.setBirthday(userResource.getBirthday());
        }

        if (userResource.getHeight() != null) {
            if (userResource.getHeight() == 0) {
                oldUser.setHeight(null);
            } else {
                oldUser.setHeight(userResource.getHeight());
            }
        }

        if (userResource.getWeight() != null) {
            if (userResource.getWeight() == 0) {
                oldUser.setWeight(null);
            } else {
                oldUser.setWeight(userResource.getWeight());
            }
        }

        return userRepository.save(oldUser);
    }

    @Override
    public User findUserById(UUID id) throws EntityDoesNotExistsException {
        return userRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("User"));
    }
}
