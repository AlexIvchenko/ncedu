package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.UserResource;
import com.ncedu.nc_edu.exceptions.AlreadyExistsException;
import com.ncedu.nc_edu.exceptions.EntityDoesNotExistsException;
import com.ncedu.nc_edu.models.*;
import com.ncedu.nc_edu.repositories.UserRepository;
import com.ncedu.nc_edu.repositories.UserRoleRepository;
import com.ncedu.nc_edu.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    private final static List<String> defaultCategories = Arrays.asList("breakfast", "lunch", "dinner");


    public UserServiceImpl(
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired UserRepository userRepository,
            @Autowired UserRoleRepository userRoleRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }


    private List<ItemCategory> getDefaultItemCategories() {
        ItemCategory breakfast = new ItemCategory();
        breakfast.setId(UUID.randomUUID());
        breakfast.setName("Breakfast");

        ItemCategory lunch = new ItemCategory();
        lunch.setId(UUID.randomUUID());
        lunch.setName("Lunch");

        ItemCategory dinner = new ItemCategory();
        dinner.setId(UUID.randomUUID());
        dinner.setName("Dinner");

        return Arrays.asList(breakfast, lunch, dinner);
    }

    @Override
    public User registerUser(String email, String password) throws AlreadyExistsException {
        password = passwordEncoder.encode(password);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistsException("User", "email");
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

        //user.setCategories(this.getDefaultItemCategories().stream()
        //        .peek(itemCategory -> itemCategory.setOwner(user)).collect(Collectors.toList()));

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
            throws EntityDoesNotExistsException, AlreadyExistsException
    {
        User oldUser = userRepository.findById(userResource.getId()).orElseThrow(() -> new EntityDoesNotExistsException("User"));

        if (userResource.getEmail() != null) {
            userRepository.findByEmail(userResource.getEmail()).orElseThrow(() -> new AlreadyExistsException("User", "email"));
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

        if (userResource.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(userResource.getPassword()));
        }

        return userRepository.save(oldUser);
    }

    @Override
    public List<Filter> getUserFiltersById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("User with id" + id))
                .getUsersFilters().stream()
                .map(UsersFilters::getFilter).collect(Collectors.toList());
    }

    @Override
    public List<UserReview> getReviewsById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("User with id" + id))
                .getReviews();
    }

    @Override
    public User findUserById(UUID id) throws EntityDoesNotExistsException {
        return userRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("User"));
    }
}
