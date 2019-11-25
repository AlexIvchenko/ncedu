package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.models.UserRole.UserRoles;
import com.ncedu.nc_edu.repositories.ReceiptRepository;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class SecurityUtils {
    @Autowired
    private ReceiptRepository receiptRepository;

    public SecurityUtils(@Autowired ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Authentication getAuthentication() {
        Authentication aht = SecurityContextHolder.getContext().getAuthentication();
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getUser() {
        return getAuthorities().contains(UserRoles.ANONYMOUS.getAuthority()) ?
                null : ((CustomUserDetails)getAuthentication().getPrincipal()).getUser();
    }

    public Set<GrantedAuthority> getAuthorities() {
        return new HashSet<>(getAuthentication().getAuthorities());
    }

    public void info() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        boolean admin = a.getAuthorities().contains(UserRoles.MODERATOR.getAuthority());
        boolean moder = a.getAuthorities().contains(UserRoles.ADMIN.getAuthority());
        System.out.println("admin " + admin);
        System.out.println("moder " + moder);
    }

    public boolean isModerator() {
        info();
        return getAuthorities().contains(UserRoles.MODERATOR.getAuthority());
    }

    public boolean isAdmin() {
        info();
        return getAuthorities().contains(UserRoles.ADMIN.getAuthority());
    }

    public boolean isAdminOrModerator() {
        info();
        Set<GrantedAuthority> authorities = getAuthorities();
        return authorities.contains(UserRoles.ADMIN.getAuthority())
                || authorities.contains(UserRoles.MODERATOR.getAuthority());
    }

    public boolean isSelfOrGranted(UUID id) {
        info();
        User user = getUser();
        return isAdminOrModerator() || (user != null && user.getId().equals(id));
    }

    public boolean isReceiptsOwnerOrGranted(UUID receiptId) {
        User user = getUser();
        return isAdminOrModerator()
                || user != null && receiptRepository.findById(receiptId)
                .get().getOwner().getId().equals(user.getId());
    }
}