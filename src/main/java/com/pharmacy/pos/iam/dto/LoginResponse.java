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
}
