package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorRequest {
    @NotBlank
    private String name;

    private String licenseNumber;

    private String phone;

    private String imageUrl;

    private String clinicName;
    private String specialty;
    private String degree;
    private Integer experienceYears;
    private Double rating;
    private Integer reviewsCount;
    private Double fee;
    private String availableSlots;
    private String availableDays;
    private String username;
    private String password;
}
