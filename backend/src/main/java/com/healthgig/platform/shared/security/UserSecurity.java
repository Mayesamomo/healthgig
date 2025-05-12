package com.healthgig.platform.shared.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // If user details is stored as principal
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            // For username based check, we would need to use a service to get user ID from username
            // This is just a placeholder - actual implementation would depend on how user info is stored
            return false; 
        }
        
        // If user ID is stored in authentication
        if (authentication.getDetails() instanceof Long) {
            return userId.equals(authentication.getDetails());
        }
        
        // For JWT, we would need to extract claims
        // This is just a placeholder - implement based on actual authentication mechanism
        return false;
    }

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_" + role));
    }
}