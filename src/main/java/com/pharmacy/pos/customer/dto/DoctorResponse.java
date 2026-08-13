package com.pharmacy.pos.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String name;
    private String licenseNumber;
    private String phone;
    private String imageUrl;
    private String clinicName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
