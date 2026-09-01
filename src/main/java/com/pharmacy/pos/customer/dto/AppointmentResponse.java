package com.pharmacy.pos.customer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private String appointmentNumber;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialty;
    private String doctorImage;
    private String patientName;
    private String patientPhone;
    private String patientEmail;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String type;
    private String status;
    private String symptoms;
    private String clinicName;
    private BigDecimal fee;
    private String qrCode;
    private LocalDateTime createdAt;
}
