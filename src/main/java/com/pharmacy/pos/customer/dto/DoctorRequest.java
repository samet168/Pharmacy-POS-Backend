package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorRequest {
    @NotBlank
    private String name;

    private String licenseNumber;

    private String phone;

    private String clinicName;
}
