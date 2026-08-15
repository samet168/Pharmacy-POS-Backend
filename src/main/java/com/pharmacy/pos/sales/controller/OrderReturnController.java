package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.sales.dto.OrderReturnRequest;
import com.pharmacy.pos.sales.dto.OrderReturnResponse;
import com.pharmacy.pos.sales.service.OrderReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-returns")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OrderReturnController {

    private final OrderReturnService orderReturnService;

    @PostMapping
    @PreAuthorize("hasAuthority('order.return')")
    @Operation(summary = "Process return", description = "Process a product return")
    public ResponseEntity<ApiResponse<OrderReturnResponse>> processReturn(@Valid @RequestBody OrderReturnRequest request) {
        OrderReturnResponse response = orderReturnService.processReturn(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Return processed successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('order.view')")
    @Operation(summary = "Get return by ID", description = "Retrieve a return by its ID")
    public ResponseEntity<ApiResponse<OrderReturnResponse>> getById(@PathVariable Long id) {
        OrderReturnResponse response = orderReturnService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Return retrieved successfully", response));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('order.view')")
    @Operation(summary = "Get returns by order", description = "Retrieve all returns for a specific order")
    public ResponseEntity<ApiResponse<List<OrderReturnResponse>>> getByOrderId(@PathVariable Long orderId) {
        List<OrderReturnResponse> responses = orderReturnService.getByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Returns retrieved successfully", responses));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('order.view')")
    @Operation(summary = "Get all returns", description = "Retrieve all returns with pagination")
    public ResponseEntity<ApiResponse<PageResponse<OrderReturnResponse>>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Returns retrieved successfully", PageResponse.of(orderReturnService.getAll(pageable))));
    }
}
