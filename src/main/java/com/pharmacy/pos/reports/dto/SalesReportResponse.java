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
public class SalesReportResponse {
    private BigDecimal totalSales;
    private BigDecimal totalRevenue;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal netSales;
    private BigDecimal averageOrderValue;
    private Long totalOrders;
    private Long refundedOrders;
    private List<PaymentMethodBreakdown> paymentMethodBreakdown;
    private List<DailySales> dailySales;
    private List<BranchSales> branchSales;
    private List<ProductSales> topProducts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodBreakdown {
        private String paymentMethod;
        private BigDecimal amount;
        private Long count;
        private BigDecimal percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailySales {
        private String date;
        private BigDecimal revenue;
        private Long orders;
        private Long customers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchSales {
        private Long branchId;
        private String branchName;
        private BigDecimal revenue;
        private Long orders;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSales {
        private Long productId;
        private String productName;
        private Long quantitySold;
        private BigDecimal revenue;
    }
}