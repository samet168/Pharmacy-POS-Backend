package com.pharmacy.pos.customer.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.customer.dto.AppointmentRequest;
import com.pharmacy.pos.customer.dto.AppointmentResponse;
import com.pharmacy.pos.customer.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Book Doctor Appointment", description = "Create a new appointment with a doctor")
    public ApiResponse<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        return ApiResponse.success("Appointment booked successfully", appointmentService.createAppointment(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(appointmentService.getById(id));
    }

    @GetMapping("/ticket/{number}")
    public ApiResponse<AppointmentResponse> getByNumber(@PathVariable String number) {
        return ApiResponse.success(appointmentService.getByNumber(number));
    }

    @GetMapping("/patient/{phone}")
    public ApiResponse<List<AppointmentResponse>> getByPatientPhone(@PathVariable String phone) {
        return ApiResponse.success(appointmentService.getByPatientPhone(phone));
    }

    @GetMapping
    public ApiResponse<PageResponse<AppointmentResponse>> getAll(Pageable pageable) {
        return ApiResponse.success(PageResponse.of(appointmentService.getAll(pageable)));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ApiResponse.success("Appointment cancelled successfully", null);
    }
}
