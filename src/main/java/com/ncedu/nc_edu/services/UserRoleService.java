package com.ncedu.nc_edu.services;


import com.ncedu.nc_edu.exceptions.RoleDoesNotExistsException;
import com.ncedu.nc_edu.models.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public interface UserRoleService {
    Set<UserRole> findRolesByUserId(UUID id);
    UserRole findById(UUID id) throws RoleDoesNotExistsException;
    UserRole findByRole(String name) throws RoleDoesNotExistsException;
}
