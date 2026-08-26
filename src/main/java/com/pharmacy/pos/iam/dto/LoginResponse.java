package com.pharmacy.pos.iam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private Long organizationId;
    private Long roleId;
    private String roleName;
    private Boolean isNewUser;
    private Boolean hasActiveSubscription;
    private String activePlanName;

    public LoginResponse(String accessToken, String refreshToken, Long userId, String username, Long organizationId, Long roleId, String roleName) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.organizationId = organizationId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.isNewUser = false;
        this.hasActiveSubscription = true;
        this.activePlanName = "Active Plan";
    }
}
