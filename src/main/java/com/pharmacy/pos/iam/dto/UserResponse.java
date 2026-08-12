package com.pharmacy.pos.iam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private Long organizationId;
    private Long roleId;
    private String name;
    private String username;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
