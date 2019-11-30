package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public interface SecurityAccessResolver {
    public Authentication getAuthentication();

    public User getUser();

    public Set<GrantedAuthority> getAuthorities();

    public boolean isModerator();

    public boolean isAdmin();

    public boolean isAdminOrModerator();

    public boolean isSelfOrGranted(UUID id);

    public boolean isRecipeOwnerOrGranted(UUID recipeId);
}
