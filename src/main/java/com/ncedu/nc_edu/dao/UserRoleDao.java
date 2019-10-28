package com.ncedu.nc_edu.dao;

import com.ncedu.nc_edu.models.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleDao extends CrudRepository<UserRole, Long> {
    UserRole findByRole(String role);
}
