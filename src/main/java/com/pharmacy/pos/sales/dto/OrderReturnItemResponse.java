package com.pharmacy.pos.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnItemResponse {

    private Long id;
    private Long orderReturnId;
    private Long orderItemId;
    private Integer quantity;
    private Boolean restock;
    private LocalDateTime createdAt;
}
