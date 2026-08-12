package com.pharmacy.pos.sales.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.common.PageResponse;
import com.pharmacy.pos.sales.dto.CheckoutRequest;
import com.pharmacy.pos.sales.dto.CheckoutResponse;
import com.pharmacy.pos.sales.dto.OrderRequest;
import com.pharmacy.pos.sales.dto.OrderResponse;
import com.pharmacy.pos.sales.entity.Order;
import com.pharmacy.pos.sales.mapper.OrderMapper;
import com.pharmacy.pos.sales.repository.OrderRepository;
import com.pharmacy.pos.sales.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('orders.create')")
    public ResponseEntity<ApiResponse<CheckoutResponse>> checkout(@Valid @RequestBody CheckoutRequest request) {
        CheckoutResponse response = checkoutService.checkout(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Checkout completed successfully", response));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('orders.create')")
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderRequest request) {
        Order order = orderMapper.toEntity(request);
        order = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", orderMapper.toResponse(order)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('orders.read')")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", orderMapper.toResponse(order)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('orders.read')")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getAll(
            @RequestParam Long organizationId,
            @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        
        Page<Order> orders;
        if (branchId != null) {
            orders = orderRepository.findByOrganizationAndBranch(organizationId, branchId, pageable);
        } else {
            orders = orderRepository.findByOrganizationId(organizationId, pageable);
        }

        PageResponse<OrderResponse> response = new PageResponse<>(
                orders.map(orderMapper::toResponse).getContent(),
                orders.getNumber(),
                orders.getTotalPages(),
                orders.getTotalElements(),
                orders.getSize(),
                orders.isFirst(),
                orders.isLast(),
                orders.isEmpty()
        );

        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('orders.update')")
    public ResponseEntity<ApiResponse<OrderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderMapper.updateEntityFromRequest(request, order);
        order = orderRepository.save(order);
        return ResponseEntity.ok(ApiResponse.success("Order updated successfully", orderMapper.toResponse(order)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('orders.delete')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
    }
}
