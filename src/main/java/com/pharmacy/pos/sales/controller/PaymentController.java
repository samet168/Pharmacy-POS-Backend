package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.sales.dto.PaymentRequest;
import com.pharmacy.pos.sales.dto.PaymentResponse;
import com.pharmacy.pos.sales.entity.Payment;
import com.pharmacy.pos.sales.mapper.PaymentMapper;
import com.pharmacy.pos.sales.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('payment.create')")
    @Operation(summary = "Create payment", description = "Create a new payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentMapper.toEntity(request);
        payment = paymentRepository.save(payment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment created successfully", paymentMapper.toResponse(payment)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('payment.view')")
    @Operation(summary = "Get payment by ID", description = "Retrieve a payment by its ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", paymentMapper.toResponse(payment)));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('payment.view')")
    @Operation(summary = "Get payments by order", description = "Retrieve all payments for a specific order")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getByOrderId(@PathVariable Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", paymentMapper.toResponseList(payments)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('payment.view')")
    @Operation(summary = "Get all payments", description = "Retrieve all payments with optional filters")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> getAll(
            @Parameter(description = "Order ID (optional)") @RequestParam(required = false) Long orderId,
            @Parameter(description = "Branch ID (optional)") @RequestParam(required = false) Long branchId,
            @Parameter(description = "Payment method (optional)") @RequestParam(required = false) String paymentMethod,
            @Parameter(description = "Status (optional)") @RequestParam(required = false) String status,
            @Parameter(description = "From date (optional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "To date (optional)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable) {
        // For now, return all payments - TODO: Add filtering logic
        Page<Payment> payments = paymentRepository.findAll(pageable);
        PageResponse<PaymentResponse> response = new PageResponse<>(
                payments.map(paymentMapper::toResponse).getContent(),
                payments.getNumber(),
                payments.getTotalPages(),
                payments.getTotalElements(),
                payments.getSize(),
                payments.isFirst(),
                payments.isLast(),
                payments.isEmpty()
        );
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('payment.update')")
    @Operation(summary = "Update payment", description = "Update an existing payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentMapper.updateEntityFromRequest(request, payment);
        payment = paymentRepository.save(payment);
        return ResponseEntity.ok(ApiResponse.success("Payment updated successfully", paymentMapper.toResponse(payment)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('payment.delete')")
    @Operation(summary = "Delete payment", description = "Delete a payment by its ID")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment deleted successfully", null));
    }
}
