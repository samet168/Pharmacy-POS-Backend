package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.sales.dto.PaymentRequest;
import com.pharmacy.pos.sales.dto.PaymentResponse;
import com.pharmacy.pos.sales.entity.Payment;
import com.pharmacy.pos.sales.mapper.PaymentMapper;
import com.pharmacy.pos.sales.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('payments.create')")
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentMapper.toEntity(request);
        payment = paymentRepository.save(payment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment created successfully", paymentMapper.toResponse(payment)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('payments.read')")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", paymentMapper.toResponse(payment)));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('payments.read')")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getByOrderId(@PathVariable Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", paymentMapper.toResponseList(payments)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('payments.update')")
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
    @PreAuthorize("hasAuthority('payments.delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment deleted successfully", null));
    }
}
