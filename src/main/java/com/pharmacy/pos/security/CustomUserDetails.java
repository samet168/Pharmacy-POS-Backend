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
            String roleUpper = user.getRole().getName().toUpperCase();
            String roleAuthority = "ROLE_" + roleUpper;
            
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            
            authorities.addAll(permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                    .collect(Collectors.toList()));
            
            authorities.add(new SimpleGrantedAuthority(roleAuthority));
            
            // Grant supreme root access to SUPERADMIN, and operational admin access to other admin roles
            if (roleUpper.contains("SUPERADMIN")) {
                authorities.add(new SimpleGrantedAuthority("SUPERADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if (roleUpper.contains("ADMIN") || roleUpper.contains("OWNER")) {
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            
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
