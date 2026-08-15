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
public class PurchaseReportResponse {
    private Long totalPurchaseOrders;
    private BigDecimal totalPurchaseValue;
    private BigDecimal receivedValue;
    private BigDecimal outstandingValue;
    private Long pendingOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private List<SupplierPurchase> supplierPurchases;
    private List<PurchaseByStatus> purchasesByStatus;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SupplierPurchase {
        private Long supplierId;
        private String supplierName;
        private BigDecimal totalValue;
        private Long orderCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseByStatus {
        private String status;
        private Long count;
        private BigDecimal value;
    }
}