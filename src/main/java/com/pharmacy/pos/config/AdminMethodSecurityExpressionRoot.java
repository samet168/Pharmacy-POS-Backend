package com.pharmacy.pos.config;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

/**
 * Delegating method-security expression root that grants the general {@code ADMIN}
 * authority access to every permission check.
 *
 * <p>The application intentionally assigns the {@code ADMIN} authority to all
 * authenticated users in development (see {@code CustomUserDetails.getAuthorities()}).
 * However, controllers are annotated with
 * {@code @PreAuthorize("hasAuthority('product.view')")}, which only checks that exact
 * permission code. When an authenticated user's role resolves without the specific
 * permission code, those checks fail with 403 even though the user holds {@code ADMIN}.
 *
 * <p>This wrapper makes {@code ADMIN} a universal bypass while delegating all other
 * checks to the standard security expression root.
 */
public class AdminMethodSecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final MethodSecurityExpressionOperations delegate;

    public AdminMethodSecurityExpressionRoot(MethodSecurityExpressionOperations delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasAuthority(String authority) {
        return delegate.hasAuthority("ADMIN") || delegate.hasAuthority("ROLE_ADMIN") || delegate.hasAuthority("SUPERADMIN") || delegate.hasAuthority("ROLE_SUPERADMIN") || delegate.hasAuthority(authority);
    }

    @Override
    public org.springframework.security.core.Authentication getAuthentication() {
        return delegate.getAuthentication();
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        return delegate.hasAuthority("ADMIN") || delegate.hasAuthority("ROLE_ADMIN") || delegate.hasAuthority("SUPERADMIN") || delegate.hasAuthority("ROLE_SUPERADMIN") || delegate.hasAnyAuthority(authorities);
    }

    @Override
    public boolean hasRole(String role) {
        return delegate.hasRole(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        return delegate.hasAnyRole(roles);
    }

    @Override
    public boolean permitAll() {
        return delegate.permitAll();
    }

    @Override
    public boolean denyAll() {
        return delegate.denyAll();
    }

    @Override
    public boolean isAnonymous() {
        return delegate.isAnonymous();
    }

    @Override
    public boolean isAuthenticated() {
        return delegate.isAuthenticated();
    }

    @Override
    public boolean isRememberMe() {
        return delegate.isRememberMe();
    }

    @Override
    public boolean isFullyAuthenticated() {
        return delegate.isFullyAuthenticated();
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return delegate.hasPermission(target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return delegate.hasPermission(targetId, targetType, permission);
    }

    @Override
    public Object getFilterObject() {
        return delegate.getFilterObject();
    }

    @Override
    public void setFilterObject(Object filterObject) {
        delegate.setFilterObject(filterObject);
    }

    @Override
    public Object getReturnObject() {
        return delegate.getReturnObject();
    }

    @Override
    public void setReturnObject(Object returnObject) {
        delegate.setReturnObject(returnObject);
    }

    @Override
    public Object getThis() {
        return delegate.getThis();
    }
}
