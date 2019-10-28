package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.dao.UserDao;
import com.ncedu.nc_edu.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserDao userDao;

    public CustomUserDetailsService(@Autowired UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public CustomUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByEmail(s);

        if (user == null) {
            throw new UsernameNotFoundException("User with given email was not found");
        }

        return new CustomUserDetails(user);
    }


}
