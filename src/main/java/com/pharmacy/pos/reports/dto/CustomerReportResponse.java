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
public class CustomerReportResponse {
    private Long totalCustomers;
    private Long newCustomers;
    private Long returningCustomers;
    private BigDecimal totalSpending;
    private BigDecimal averageSpending;
    private List<TopCustomer> topCustomers;
    private List<CustomerSpendingByPeriod> spendingByPeriod;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopCustomer {
        private Long customerId;
        private String customerName;
        private String phone;
        private BigDecimal totalSpending;
        private Long orderCount;
        private BigDecimal averageOrderValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerSpendingByPeriod {
        private String period;
        private Long customerCount;
        private BigDecimal totalSpending;
    }
}