package com.pharmacy.pos.iam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private Long id;
    private String code;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
