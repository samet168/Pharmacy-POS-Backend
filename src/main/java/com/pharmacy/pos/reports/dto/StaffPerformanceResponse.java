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
public class StaffPerformanceResponse {
    private List<StaffPerformance> staffPerformanceList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffPerformance {
        private Long userId;
        private String userName;
        private String userRole;
        private Long ordersProcessed;
        private BigDecimal totalSales;
        private BigDecimal averageOrderValue;
        private Long refundsProcessed;
        private BigDecimal refundAmount;
    }
}