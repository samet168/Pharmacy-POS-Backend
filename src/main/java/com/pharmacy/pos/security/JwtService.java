package com.pharmacy.pos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiry-minutes}")
    private long accessTokenExpiryMinutes;

    @Value("${jwt.refresh-token-expiry-days}")
    private long refreshTokenExpiryDays;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(Long userId, Long organizationId, Long roleId, List<Long> branchIds) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiryMinutes * 60 * 1000))
                .claims(Map.of(
                        "organizationId", organizationId,
                        "roleId", roleId,
                        "branchIds", branchIds,
                        "type", "access"
                ))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiryDays * 24 * 60 * 60 * 1000))
                .claims(Map.of("type", "refresh"))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public Long extractOrganizationId(String token) {
        Object orgId = extractClaims(token).get("organizationId");
        if (orgId instanceof Integer) {
            return ((Integer) orgId).longValue();
        }
        return (Long) orgId;
    }

    public Long extractRoleId(String token) {
        Object roleId = extractClaims(token).get("roleId");
        if (roleId instanceof Integer) {
            return ((Integer) roleId).longValue();
        }
        return (Long) roleId;
    }

    @SuppressWarnings("unchecked")
    public List<Long> extractBranchIds(String token) {
        List<?> branchIds = extractClaims(token).get("branchIds", List.class);
        if (branchIds == null) {
            return List.of();
        }
        return branchIds.stream()
                .map(id -> {
                    if (id instanceof Integer) {
                        return ((Integer) id).longValue();
                    }
                    return (Long) id;
                })
                .toList();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        return "access".equals(extractClaims(token).get("type"));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractClaims(token).get("type"));
    }
}
