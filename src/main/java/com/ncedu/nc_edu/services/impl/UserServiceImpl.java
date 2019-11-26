package com.ncedu.nc_edu.services.impl;

import com.ncedu.nc_edu.dto.resources.UserInfoResource;
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

//        user.setCategories(this.getDefaultItemCategories().stream()
//                .peek(itemCategory -> itemCategory.setOwner(user)).collect(Collectors.toList()));

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
    public User update(UserInfoResource userInfo)
            throws EntityDoesNotExistsException, AlreadyExistsException
    {
        User oldUser = userRepository.findById(userInfo.getId()).orElseThrow(() -> new EntityDoesNotExistsException("User"));

        if (userInfo.getEmail() != null) {
            if (userRepository.findByEmail(userInfo.getEmail()).isPresent())
                throw new AlreadyExistsException("User", "email");
            oldUser.setEmail(userInfo.getEmail());
        }

        if (userInfo.getUsername() != null) {
            oldUser.setUsername(userInfo.getUsername());
        }

        if (userInfo.getGender() != null) {
            oldUser.setGender(User.Gender.valueOf(userInfo.getGender()));
        }

        if (userInfo.getBirthday() != null) {
            oldUser.setBirthday(userInfo.getBirthday());
        }

        if (userInfo.getHeight() != null) {
            if (userInfo.getHeight() == 0) {
                oldUser.setHeight(null);
            } else {
                oldUser.setHeight(userInfo.getHeight());
            }
        }

        if (userInfo.getWeight() != null) {
            if (userInfo.getWeight() == 0) {
                oldUser.setWeight(null);
            } else {
                oldUser.setWeight(userInfo.getWeight());
            }
        }

        if (userInfo.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        }

        return userRepository.save(oldUser);
    }

    @Override
    public List<Receipt> getReceiptsById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("User with id " + id))
                .getReceipts();
    }

    @Override
    public List<UserReview> getReviewsById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("User with id " + id))
                .getReviews();
    }

    @Override
    public User findUserById(UUID id) throws EntityDoesNotExistsException {
        return userRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistsException("User"));
    }


}
