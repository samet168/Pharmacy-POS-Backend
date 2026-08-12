package com.pharmacy.pos.security;

import com.pharmacy.pos.common.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        final Long userId;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token) || !jwtService.isAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        userId = jwtService.extractUserId(token);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserById(userId);
            var authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            Long organizationId = jwtService.extractOrganizationId(token);
            Long roleId = jwtService.extractRoleId(token);
            List<Long> branchIds = jwtService.extractBranchIds(token);

            TenantContext.setOrganizationId(organizationId);
            TenantContext.setUserId(userId);
            TenantContext.setRoleId(roleId);
            if (branchIds != null && !branchIds.isEmpty()) {
                TenantContext.setBranchId(branchIds.get(0));
            }
        }

        filterChain.doFilter(request, response);
    }
}
