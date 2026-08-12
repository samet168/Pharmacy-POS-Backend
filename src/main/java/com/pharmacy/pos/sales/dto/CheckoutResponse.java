package com.pharmacy.pos.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private OrderResponse order;
    private List<OrderItemResponse> items;
    private List<PaymentResponse> payments;
    private List<String> allergyWarnings;
    private String message;
}
