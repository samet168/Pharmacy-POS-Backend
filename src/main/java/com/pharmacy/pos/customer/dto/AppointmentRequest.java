package com.pharmacy.pos.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AppointmentRequest {
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Patient phone is required")
    private String patientPhone;

    private String patientEmail;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotBlank(message = "Appointment time is required")
    private String appointmentTime;

    private String type; // IN_PERSON or TELE_CONSULT

    private String symptoms;
    private String clinicName;
    private String branchName;
    private BigDecimal fee;
}
