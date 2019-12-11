package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

public interface SecurityAccessResolver {
    Authentication getAuthentication();

    User getUser();

    Set<GrantedAuthority> getAuthorities();

    boolean isSelf(UUID id);

    boolean isModerator();

    boolean isAdmin();

    boolean isAdminOrModerator();

    boolean isSelfOrGranted(UUID id);

    boolean isRecipeOwnerOrGranted(UUID recipeId);

    GrantedAuthority getHeadAuthority();
}
