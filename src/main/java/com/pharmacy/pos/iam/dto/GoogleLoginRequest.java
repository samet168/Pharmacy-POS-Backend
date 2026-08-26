package com.pharmacy.pos.iam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {

    @NotBlank(message = "Email or Google ID is required")
    private String email;

    private String name;
    private String picture;
    private String googleId;
    private String idToken;
    private Long organizationId;
    private Long branchId;
}
