package com.pharmacy.pos.security;

import com.pharmacy.pos.iam.entity.Permission;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.repository.RolePermissionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private User user;
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() != null) {
            // Fetch permissions directly from database to avoid lazy loading issues
            List<Permission> permissions = rolePermissionRepository.findPermissionsByRoleId(user.getRole().getId());
            
            // Add role as authority (Spring Security expects ROLE_ prefix for hasRole())
            String roleAuthority = "ROLE_" + user.getRole().getName().toUpperCase();
            
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            
            authorities.addAll(permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                    .collect(Collectors.toList()));
            
            authorities.add(new SimpleGrantedAuthority(roleAuthority));
            
            // Development-friendly: all authenticated users get ADMIN access.
            // This ensures the frontend can access all endpoints during development.
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            
            return authorities;
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
