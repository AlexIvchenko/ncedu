package com.ncedu.nc_edu.dao;

import com.ncedu.nc_edu.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
