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
public class ProductReportResponse {
    private Long totalProducts;
    private Long activeProducts;
    private Long inactiveProducts;
    private Long lowStockProducts;
    private Long outOfStockProducts;
    private Long nearExpiryProducts;
    private Long expiredProducts;
    private List<TopSellingProduct> topSellingProducts;
    private List<LowStockProduct> lowStockProductsList;
    private List<ExpiringProduct> expiringProductsList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopSellingProduct {
        private Long productId;
        private String productName;
        private String sku;
        private Long quantitySold;
        private BigDecimal revenue;
        private BigDecimal profit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LowStockProduct {
        private Long productId;
        private String productName;
        private String sku;
        private Integer currentStock;
        private Integer minimumStock;
        private Integer reorderLevel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpiringProduct {
        private Long productId;
        private String productName;
        private String batchNumber;
        private Integer quantity;
        private String expiryDate;
        private Integer daysUntilExpiry;
    }
}