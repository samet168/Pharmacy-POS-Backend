package com.pharmacy.pos.reports.service;

import com.pharmacy.pos.reports.dto.*;
import com.pharmacy.pos.sales.entity.Order;
import com.pharmacy.pos.sales.entity.Payment;
import com.pharmacy.pos.sales.entity.OrderItem;
import com.pharmacy.pos.sales.repository.OrderRepository;
import com.pharmacy.pos.sales.repository.PaymentRepository;
import com.pharmacy.pos.sales.repository.OrderItemRepository;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.inventory.repository.BranchInventoryRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.inventory.entity.BranchInventory;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.customer.repository.CustomerRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.purchasing.repository.PurchaseOrderRepository;
import com.pharmacy.pos.purchasing.entity.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final ProductBatchRepository productBatchRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public SalesReportResponse getSalesReport(Long organizationId, Long branchId, LocalDate from, LocalDate to) {
        LocalDateTime startDate = from != null ? from.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime endDate = to != null ? to.atTime(LocalTime.MAX) : LocalDateTime.now();

        List<Order> orders = orderRepository.findByOrganizationIdAndCreatedAtBetween(
                organizationId, startDate, endDate);

        if (branchId != null) {
            orders = orders.stream()
                    .filter(order -> order.getBranch() != null && order.getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
        }

        BigDecimal totalSales = orders.stream()
                .map(Order::getGrandTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRevenue = totalSales; // Simplified - in real implementation, calculate revenue after discounts
        BigDecimal averageOrderValue = orders.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);

        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<Payment> payments = orderIds.isEmpty() ? new ArrayList<>() : paymentRepository.findByOrderIdIn(orderIds);

        Map<String, List<Payment>> paymentsByMethod = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentMethod().name()));

        List<SalesReportResponse.PaymentMethodBreakdown> paymentBreakdown = paymentsByMethod.entrySet().stream()
                .map(entry -> {
                    String method = entry.getKey();
                    List<Payment> methodPayments = entry.getValue();
                    BigDecimal amount = methodPayments.stream()
                            .map(Payment::getAmountPaid)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return SalesReportResponse.PaymentMethodBreakdown.builder()
                            .paymentMethod(method)
                            .amount(amount)
                            .count((long) methodPayments.size())
                            .percentage(totalSales.compareTo(BigDecimal.ZERO) > 0 ?
                                    amount.divide(totalSales, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                                    BigDecimal.ZERO)
                            .build();
                })
                .collect(Collectors.toList());

        return SalesReportResponse.builder()
                .totalSales(totalSales)
                .totalRevenue(totalRevenue)
                .totalDiscount(BigDecimal.ZERO) // Calculate from orders
                .totalTax(BigDecimal.ZERO) // Calculate from orders
                .netSales(totalRevenue)
                .averageOrderValue(averageOrderValue)
                .totalOrders((long) orders.size())
                .refundedOrders(0L) // Calculate from OrderReturn
                .paymentMethodBreakdown(paymentBreakdown)
                .dailySales(calculateDailySales(orders))
                .branchSales(calculateBranchSales(orders))
                .topProducts(calculateTopProducts(orders))
                .build();
    }

    public ProductReportResponse getProductReport(Long organizationId, Long branchId, LocalDate from, LocalDate to) {
        List<Product> products = productRepository.findByOrganizationId(organizationId);
        long activeProducts = products.stream().filter(Product::isActive).count();
        long inactiveProducts = products.size() - activeProducts;

        // Calculate low stock from branch inventory
        List<BranchInventory> inventories = branchId != null ?
                branchInventoryRepository.findByBranchId(branchId) :
                new ArrayList<>(); // Need to add organization filter to repository

        long lowStockCount = inventories.stream()
                .filter(inv -> inv.getQuantityInBaseUnit() <= inv.getBatch().getProduct().getMinStockAlert())
                .count();

        // Check expiring products
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysFromNow = today.plusDays(30);
        List<ProductBatch> expiringBatches = productBatchRepository.findByExpiryDateBetween(today, thirtyDaysFromNow);
        long nearExpiryCount = expiringBatches.size();
        long expiredCount = productBatchRepository.findByExpiryDateBefore(today).size();

        // Date range for filtering order-based stats (default: last 30 days)
        LocalDateTime startDate = from != null ? from.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime endDate = to != null ? to.atTime(LocalTime.MAX) : LocalDateTime.now();

        // Fetch orders to compute top-selling products
        List<Order> orders = orderRepository.findByOrganizationIdAndCreatedAtBetween(organizationId, startDate, endDate);
        if (branchId != null) {
            orders = orders.stream()
                    .filter(order -> order.getBranch() != null && order.getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
        }

        return ProductReportResponse.builder()
                .totalProducts((long) products.size())
                .activeProducts(activeProducts)
                .inactiveProducts(inactiveProducts)
                .lowStockProducts(lowStockCount)
                .outOfStockProducts(0L) // Calculate from inventory
                .nearExpiryProducts(nearExpiryCount)
                .expiredProducts(expiredCount)
                .topSellingProducts(calculateTopSellingProducts(orders))
                .lowStockProductsList(calculateLowStockProducts(inventories))
                .expiringProductsList(calculateExpiringProducts(expiringBatches))
                .build();
    }

    private List<ProductReportResponse.TopSellingProduct> calculateTopSellingProducts(List<Order> orders) {
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> orderItems = orderIds.isEmpty() ? new ArrayList<>() : orderItemRepository.findByOrderIdIn(orderIds);

        Map<Long, List<OrderItem>> itemsByProduct = orderItems.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getId()));

        return itemsByProduct.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    List<OrderItem> productItems = entry.getValue();
                    long quantitySold = productItems.stream().mapToLong(item -> (long) item.getQuantity()).sum();
                    BigDecimal revenue = productItems.stream()
                            .map(OrderItem::getSubtotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal cost = productItems.stream()
                            .map(item -> {
                                BigDecimal unitCost = (item.getUnit() != null && item.getUnit().getCostPrice() != null)
                                        ? item.getUnit().getCostPrice()
                                        : BigDecimal.ZERO;
                                return unitCost.multiply(BigDecimal.valueOf(item.getQuantity()));
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal profit = revenue.subtract(cost);
                    Product product = productItems.get(0).getProduct();
                    return ProductReportResponse.TopSellingProduct.builder()
                            .productId(productId)
                            .productName(product.getBrandName())
                            .sku(product.getSku())
                            .quantitySold(quantitySold)
                            .revenue(revenue.setScale(2, RoundingMode.HALF_UP))
                            .profit(profit.setScale(2, RoundingMode.HALF_UP))
                            .build();
                })
                .sorted(Comparator.comparing(ProductReportResponse.TopSellingProduct::getRevenue).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<ProductReportResponse.LowStockProduct> calculateLowStockProducts(List<BranchInventory> inventories) {
        return inventories.stream()
                .filter(inv -> inv.getQuantityInBaseUnit() <= inv.getBatch().getProduct().getMinStockAlert())
                .map(inv -> {
                    Product product = inv.getBatch().getProduct();
                    return ProductReportResponse.LowStockProduct.builder()
                            .productId(product.getId())
                            .productName(product.getBrandName())
                            .sku(product.getSku())
                            .currentStock(inv.getQuantityInBaseUnit())
                            .minimumStock(product.getMinStockAlert())
                            .reorderLevel(product.getMinStockAlert())
                            .build();
                })
                .limit(20)
                .collect(Collectors.toList());
    }

    private List<ProductReportResponse.ExpiringProduct> calculateExpiringProducts(List<ProductBatch> batches) {
        LocalDate today = LocalDate.now();
        return batches.stream()
                .map(batch -> {
                    Product product = batch.getProduct();
                    int daysUntilExpiry = batch.getExpiryDate() != null
                            ? (int) ChronoUnit.DAYS.between(today, batch.getExpiryDate())
                            : 0;
                    // quantity intentionally left null/0 to avoid touching the lazy branchInventories collection
                    return ProductReportResponse.ExpiringProduct.builder()
                            .productId(product.getId())
                            .productName(product.getBrandName())
                            .batchNumber(batch.getBatchNumber())
                            .expiryDate(batch.getExpiryDate() != null ? batch.getExpiryDate().toString() : null)
                            .daysUntilExpiry(daysUntilExpiry)
                            .build();
                })
                .limit(20)
                .collect(Collectors.toList());
    }


    public CustomerReportResponse getCustomerReport(Long organizationId, Long branchId, LocalDate from, LocalDate to) {
        LocalDateTime startDate = from != null ? from.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime endDate = to != null ? to.atTime(LocalTime.MAX) : LocalDateTime.now();

        var customers = customerRepository.findByOrganizationId(organizationId);
        long totalCustomers = customers.size();

        // Calculate new customers in period
        long newCustomers = customers.stream()
                .filter(c -> c.getCreatedAt().isAfter(startDate) && c.getCreatedAt().isBefore(endDate))
                .count();

        return CustomerReportResponse.builder()
                .totalCustomers(totalCustomers)
                .newCustomers(newCustomers)
                .returningCustomers(0L) // Calculate from orders
                .totalSpending(BigDecimal.ZERO) // Calculate from orders
                .averageSpending(BigDecimal.ZERO) // Calculate from orders
                .topCustomers(new ArrayList<>()) // Populate from order analysis
                .spendingByPeriod(new ArrayList<>()) // Populate from customer creation dates
                .build();
    }

    public PurchaseReportResponse getPurchaseReport(Long organizationId, Long branchId, LocalDate from, LocalDate to) {
        LocalDateTime startDate = from != null ? from.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime endDate = to != null ? to.atTime(LocalTime.MAX) : LocalDateTime.now();

        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByOrganizationIdAndCreatedAtBetween(
                organizationId, startDate, endDate);

        if (branchId != null) {
            purchaseOrders = purchaseOrders.stream()
                    .filter(po -> po.getBranch() != null && po.getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
        }

        BigDecimal totalValue = purchaseOrders.stream()
                .map(PurchaseOrder::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount = purchaseOrders.stream()
                .filter(po -> po.getStatus() == com.pharmacy.pos.common.enums.PurchaseStatus.ORDERED)
                .count();
        long completedCount = purchaseOrders.stream()
                .filter(po -> po.getStatus() == com.pharmacy.pos.common.enums.PurchaseStatus.RECEIVED)
                .count();
        long cancelledCount = purchaseOrders.stream()
                .filter(po -> po.getStatus() == com.pharmacy.pos.common.enums.PurchaseStatus.CANCELLED)
                .count();

        return PurchaseReportResponse.builder()
                .totalPurchaseOrders((long) purchaseOrders.size())
                .totalPurchaseValue(totalValue)
                .receivedValue(BigDecimal.ZERO) // Calculate from goods receipts
                .outstandingValue(totalValue) // Simplified
                .pendingOrders(pendingCount)
                .completedOrders(completedCount)
                .cancelledOrders(cancelledCount)
                .supplierPurchases(new ArrayList<>()) // Populate by supplier
                .purchasesByStatus(new ArrayList<>()) // Populate by status
                .build();
    }

    public InventoryReportResponse getInventoryReport(Long organizationId, Long branchId) {
        List<BranchInventory> inventories = branchId != null ?
                branchInventoryRepository.findByBranchId(branchId) :
                branchInventoryRepository.findByBranchOrganizationId(organizationId);

        BigDecimal totalValue = BigDecimal.ZERO;
        long totalQuantity = 0;
        long lowStockCount = 0;
        long outOfStockCount = 0;

        for (BranchInventory inv : inventories) {
            // Simplified calculation - in real implementation, use actual cost from batch
            totalValue = totalValue.add(BigDecimal.valueOf(inv.getQuantityInBaseUnit()));
            totalQuantity += inv.getQuantityInBaseUnit();
            if (inv.getQuantityInBaseUnit() <= inv.getBatch().getProduct().getMinStockAlert()) {
                lowStockCount++;
            }
            if (inv.getQuantityInBaseUnit() == 0) {
                outOfStockCount++;
            }
        }

        // Calculate branch stocks
        Map<Long, List<BranchInventory>> byBranch = inventories.stream()
                .filter(inv -> inv.getBranch() != null)
                .collect(Collectors.groupingBy(inv -> inv.getBranch().getId()));

        List<InventoryReportResponse.BranchStock> branchStocks = byBranch.entrySet().stream()
                .map(entry -> {
                    Long bId = entry.getKey();
                    List<BranchInventory> branchInvs = entry.getValue();
                    String branchName = branchInvs.get(0).getBranch().getName();
                    long branchTotalQuantity = branchInvs.stream().mapToLong(BranchInventory::getQuantityInBaseUnit).sum();
                    BigDecimal branchTotalValue = branchInvs.stream()
                            .map(inv -> BigDecimal.valueOf(inv.getQuantityInBaseUnit()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    long branchLowStock = branchInvs.stream()
                            .filter(inv -> inv.getQuantityInBaseUnit() <= inv.getBatch().getProduct().getMinStockAlert())
                            .count();
                    
                    return InventoryReportResponse.BranchStock.builder()
                            .branchId(bId)
                            .branchName(branchName)
                            .totalProducts((long) branchInvs.stream().map(inv -> inv.getBatch().getProduct().getId()).distinct().count())
                            .stockValue(branchTotalValue)
                            .lowStockCount(branchLowStock)
                            .build();
                })
                .collect(Collectors.toList());

        return InventoryReportResponse.builder()
                .totalStockValue(totalValue)
                .totalStockQuantity(totalQuantity)
                .lowStockCount(lowStockCount)
                .outOfStockCount(outOfStockCount)
                .expiringCount(0L) // Calculate from batches
                .expiredCount(0L) // Calculate from batches
                .branchStocks(branchStocks)
                .categoryStocks(new ArrayList<>()) // Populate by category
                .stockMovementSummary(new ArrayList<>()) // Populate from stock movements
                .build();
    }

    public StaffPerformanceResponse getStaffPerformanceReport(Long organizationId, Long branchId, LocalDate from, LocalDate to) {
        LocalDateTime startDate = from != null ? from.atStartOfDay() : LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime endDate = to != null ? to.atTime(LocalTime.MAX) : LocalDateTime.now();

        List<Order> orders = orderRepository.findByOrganizationIdAndCreatedAtBetween(
                organizationId, startDate, endDate);

        if (branchId != null) {
            orders = orders.stream()
                    .filter(order -> order.getBranch() != null && order.getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
        }

        // Group by user who created the order
        Map<Long, List<Order>> ordersByUser = orders.stream()
                .filter(order -> order.getUser() != null)
                .collect(Collectors.groupingBy(order -> order.getUser().getId()));

        List<StaffPerformanceResponse.StaffPerformance> staffPerformanceList = ordersByUser.entrySet().stream()
                .map(entry -> {
                    Long userId = entry.getKey();
                    List<Order> userOrders = entry.getValue();
                    BigDecimal totalSales = userOrders.stream()
                            .map(Order::getGrandTotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal avgOrderValue = userOrders.isEmpty() ? BigDecimal.ZERO :
                            totalSales.divide(BigDecimal.valueOf(userOrders.size()), 2, RoundingMode.HALF_UP);

                    return StaffPerformanceResponse.StaffPerformance.builder()
                            .userId(userId)
                            .userName("User " + userId) // Populate from User entity
                            .userRole("Staff") // Populate from User entity
                            .ordersProcessed((long) userOrders.size())
                            .totalSales(totalSales)
                            .averageOrderValue(avgOrderValue)
                            .refundsProcessed(0L) // Calculate from OrderReturn
                            .refundAmount(BigDecimal.ZERO) // Calculate from OrderReturn
                            .build();
                })
                .collect(Collectors.toList());

        return StaffPerformanceResponse.builder()
                .staffPerformanceList(staffPerformanceList)
                .build();
    }

    private List<SalesReportResponse.DailySales> calculateDailySales(List<Order> orders) {
        Map<LocalDate, List<Order>> ordersByDate = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().toLocalDate()));

        return ordersByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Order> dayOrders = entry.getValue();
                    BigDecimal revenue = dayOrders.stream()
                            .map(Order::getGrandTotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return SalesReportResponse.DailySales.builder()
                            .date(date.toString())
                            .revenue(revenue)
                            .orders((long) dayOrders.size())
                            .customers((long) dayOrders.stream()
                                    .map(Order::getCustomer)
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .count())
                            .build();
                })
                .sorted(Comparator.comparing(SalesReportResponse.DailySales::getDate))
                .collect(Collectors.toList());
    }

    private List<SalesReportResponse.BranchSales> calculateBranchSales(List<Order> orders) {
        Map<Long, List<Order>> ordersByBranch = orders.stream()
                .filter(order -> order.getBranch() != null)
                .collect(Collectors.groupingBy(order -> order.getBranch().getId()));

        return ordersByBranch.entrySet().stream()
                .map(entry -> {
                    Long branchId = entry.getKey();
                    List<Order> branchOrders = entry.getValue();
                    BigDecimal revenue = branchOrders.stream()
                            .map(Order::getGrandTotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return SalesReportResponse.BranchSales.builder()
                            .branchId(branchId)
                            .branchName("Branch " + branchId) // Populate from Branch entity
                            .revenue(revenue)
                            .orders((long) branchOrders.size())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<SalesReportResponse.ProductSales> calculateTopProducts(List<Order> orders) {
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> orderItems = orderIds.isEmpty() ? new ArrayList<>() : orderItemRepository.findByOrderIdIn(orderIds);

        Map<Long, List<OrderItem>> itemsByProduct = orderItems.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getId()));

        return itemsByProduct.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    List<OrderItem> productItems = entry.getValue();
                    long quantitySold = productItems.stream()
                            .mapToLong(OrderItem::getQuantity)
                            .sum();
                    BigDecimal revenue = productItems.stream()
                            .map(OrderItem::getSubtotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return SalesReportResponse.ProductSales.builder()
                            .productId(productId)
                            .productName(productItems.get(0).getProduct().getBrandName())
                            .quantitySold(quantitySold)
                            .revenue(revenue)
                            .build();
                })
                .sorted(Comparator.comparing(SalesReportResponse.ProductSales::getRevenue).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}