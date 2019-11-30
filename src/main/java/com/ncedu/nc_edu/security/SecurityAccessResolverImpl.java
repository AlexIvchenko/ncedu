package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole.UserRoles;
import com.ncedu.nc_edu.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class SecurityAccessResolverImpl implements SecurityAccessResolver{
    private RecipeRepository recipeRepository;

    public SecurityAccessResolverImpl(@Autowired RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getUser() {
        return getAuthorities().contains(UserRoles.ANONYMOUS.getAuthority()) ?
                null : ((CustomUserDetails)getAuthentication().getPrincipal()).getUser();
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return new HashSet<>(getAuthentication().getAuthorities());
    }

    @Override
    public boolean isModerator() {
        return getAuthorities().contains(UserRoles.MODERATOR.getAuthority());
    }

    @Override
    public boolean isAdmin() {
        return getAuthorities().contains(UserRoles.ADMIN.getAuthority());
    }

    @Override
    public boolean isAdminOrModerator() {
        Set<GrantedAuthority> authorities = getAuthorities();
        return authorities.contains(UserRoles.ADMIN.getAuthority())
                || authorities.contains(UserRoles.MODERATOR.getAuthority());
    }

    @Override
    public boolean isSelfOrGranted(UUID id) {
        User user = getUser();
        return isAdminOrModerator() || (user != null && user.getId().equals(id));
    }

    @Override
    public boolean isRecipeOwnerOrGranted(UUID recipeId) {
        User user = getUser();
        return isAdminOrModerator()
                || user != null && recipeRepository.findById(recipeId)
                .get().getOwner().getId().equals(user.getId());
    }
}