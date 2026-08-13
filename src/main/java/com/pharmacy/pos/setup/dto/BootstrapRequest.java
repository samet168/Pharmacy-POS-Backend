package com.pharmacy.pos.setup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BootstrapRequest {
    
    @NotBlank(message = "Organization name is required")
    @Size(max = 100, message = "Organization name must be less than 100 characters")
    private String organizationName;
    
    @NotBlank(message = "Organization slug is required")
    @Size(max = 50, message = "Organization slug must be less than 50 characters")
    private String organizationSlug;
    
    @NotBlank(message = "Branch name is required")
    @Size(max = 100, message = "Branch name must be less than 100 characters")
    private String branchName;
    
    @NotBlank(message = "Branch code is required")
    @Size(max = 20, message = "Branch code must be less than 20 characters")
    private String branchCode;
    
    @NotBlank(message = "Admin username is required")
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String adminUsername;
    
    @NotBlank(message = "Admin password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String adminPassword;
    
    @NotBlank(message = "Admin name is required")
    @Size(max = 100, message = "Admin name must be less than 100 characters")
    private String adminName;
    
    @NotBlank(message = "Admin PIN code is required")
    @Size(min = 4, max = 6, message = "PIN code must be between 4 and 6 characters")
    private String adminPinCode;
}