package com.pharmacy.pos.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * Registers a custom {@link MethodSecurityExpressionHandler} so that method-level
 * {@code @PreAuthorize} checks use {@link AdminMethodSecurityExpressionRoot}.
 *
 * <p>This is the single-point "ADMIN authority bypass" for method security: any
 * authenticated user carrying the {@code ADMIN} authority (granted to every
 * authenticated user in development) passes all {@code hasAuthority(...)} checks.
 */
@Configuration
public class MethodSecurityConfig {

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler() {
            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
                    Authentication authentication, MethodInvocation invocation) {
                MethodSecurityExpressionOperations base =
                        super.createSecurityExpressionRoot(authentication, invocation);
                return new AdminMethodSecurityExpressionRoot(base);
            }
        };
    }
}

