package com.pharmacy.pos.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReportResponse {
    private BigDecimal totalStockValue;
    private Long totalStockQuantity;
    private Long lowStockCount;
    private Long outOfStockCount;
    private Long expiringCount;
    private Long expiredCount;
    private List<BranchStock> branchStocks;
    private List<CategoryStock> categoryStocks;
    private List<StockMovementSummary> stockMovementSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchStock {
        private Long branchId;
        private String branchName;
        private BigDecimal stockValue;
        private Long totalProducts;
        private Long lowStockCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStock {
        private Long categoryId;
        private String categoryName;
        private BigDecimal stockValue;
        private Long productCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockMovementSummary {
        private String movementType;
        private Long count;
        private BigDecimal quantity;
    }
}