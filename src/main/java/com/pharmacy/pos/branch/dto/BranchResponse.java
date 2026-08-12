package com.pharmacy.pos.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    private Long id;
    private Long organizationId;
    private String code;
    private String name;
    private String location;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
