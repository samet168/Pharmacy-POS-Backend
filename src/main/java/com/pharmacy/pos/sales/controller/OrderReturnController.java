package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.sales.dto.OrderReturnRequest;
import com.pharmacy.pos.sales.dto.OrderReturnResponse;
import com.pharmacy.pos.sales.service.OrderReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-returns")
@RequiredArgsConstructor
public class OrderReturnController {

    private final OrderReturnService orderReturnService;

    @PostMapping
    @PreAuthorize("hasAuthority('order.return')")
    public ResponseEntity<ApiResponse<OrderReturnResponse>> processReturn(@Valid @RequestBody OrderReturnRequest request) {
        OrderReturnResponse response = orderReturnService.processReturn(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Return processed successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('order.view')")
    public ResponseEntity<ApiResponse<OrderReturnResponse>> getById(@PathVariable Long id) {
        OrderReturnResponse response = orderReturnService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Return retrieved successfully", response));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('order.view')")
    public ResponseEntity<ApiResponse<List<OrderReturnResponse>>> getByOrderId(@PathVariable Long orderId) {
        List<OrderReturnResponse> responses = orderReturnService.getByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Returns retrieved successfully", responses));
    }
}
