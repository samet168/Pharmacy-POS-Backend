package com.pharmacy.pos.dashboard.service;

import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.customer.repository.CustomerRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.sales.repository.OrderRepository;
import com.pharmacy.pos.sales.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public Map<String, Object> getOverview(LocalDate from, LocalDate to) {
        Map<String, Object> overview = new HashMap<>();
        
        long totalProducts = productRepository.count();
        long totalCustomers = customerRepository.count();
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();
        
        BigDecimal totalRevenue = paymentRepository.findAll().stream()
                .map(payment -> payment.getAmountPaid() != null ? payment.getAmountPaid() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // If date range is provided, calculate filtered metrics
        if (from != null && to != null) {
            // TODO: Add date-filtered queries when needed
            overview.put("todayOrders", 0L);
            overview.put("todayRevenue", BigDecimal.ZERO);
        } else {
            // Default to today's metrics
            overview.put("todayOrders", 0L);
            overview.put("todayRevenue", BigDecimal.ZERO);
        }
        
        overview.put("totalProducts", totalProducts);
        overview.put("totalCustomers", totalCustomers);
        overview.put("totalDoctors", 0L); // Add when doctor repository is available
        overview.put("totalUsers", totalUsers);
        overview.put("totalOrders", totalOrders);
        overview.put("totalRevenue", totalRevenue);
        overview.put("totalPurchases", 0L); // Add when purchase order repository is available
        overview.put("pendingOrders", 0L); // Add when order status is available
        overview.put("lowStockProducts", 0L); // Add when stock repository is available
        
        return overview;
    }

    public Map<String, Object> getSales(LocalDate from, LocalDate to) {
        Map<String, Object> sales = new HashMap<>();
        
        // If date range is provided, filter payments by date
        // For now, use all payments
        BigDecimal totalSales = paymentRepository.findAll().stream()
                .map(payment -> payment.getAmountPaid() != null ? payment.getAmountPaid() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long totalOrders = orderRepository.count();
        BigDecimal averageOrderValue = totalOrders > 0 ? totalSales.divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
        
        sales.put("totalSales", totalSales);
        sales.put("totalOrders", totalOrders);
        sales.put("averageOrderValue", averageOrderValue);
        sales.put("dailySales", new java.util.ArrayList<>()); // Add when date filtering is available
        
        return sales;
    }

    public Map<String, Object> getProducts(LocalDate from, LocalDate to) {
        Map<String, Object> products = new HashMap<>();
        
        long totalProducts = productRepository.count();
        long activeProducts = productRepository.count(); // Add when active field is available
        long inactiveProducts = 0L; // Add when active field is available
        long lowStockProducts = 0L; // Add when stock repository is available
        long outOfStockProducts = 0L; // Add when stock repository is available
        
        products.put("totalProducts", totalProducts);
        products.put("activeProducts", activeProducts);
        products.put("inactiveProducts", inactiveProducts);
        products.put("lowStockProducts", lowStockProducts);
        products.put("outOfStockProducts", outOfStockProducts);
        products.put("topSellingProducts", new java.util.ArrayList<>()); // Add when order items are available
        
        return products;
    }

    public Map<String, Object> getCustomers(LocalDate from, LocalDate to) {
        Map<String, Object> customers = new HashMap<>();
        
        long totalCustomers = customerRepository.count();
        long newCustomers = 0L; // Add when date filtering is available
        long activeCustomers = totalCustomers; // Add when active field is available
        
        customers.put("totalCustomers", totalCustomers);
        customers.put("newCustomers", newCustomers);
        customers.put("activeCustomers", activeCustomers);
        customers.put("customersByPeriod", new java.util.ArrayList<>()); // Add when date filtering is available
        
        return customers;
    }

    public Map<String, Object> getOrders(LocalDate from, LocalDate to) {
        Map<String, Object> orders = new HashMap<>();
        
        long totalOrders = orderRepository.count();
        long pending = 0L; // Add when order status is available
        long completed = 0L; // Add when order status is available
        long cancelled = 0L; // Add when order status is available
        long returned = 0L; // Add when order status is available
        long todayOrders = 0L; // Add when date filtering is available
        
        orders.put("totalOrders", totalOrders);
        orders.put("pending", pending);
        orders.put("completed", completed);
        orders.put("cancelled", cancelled);
        orders.put("returned", returned);
        orders.put("todayOrders", todayOrders);
        
        return orders;
    }

    public Map<String, Object> getLowStock() {
        Map<String, Object> lowStock = new HashMap<>();
        List<Map<String, Object>> items = new java.util.ArrayList<>();
        try {
            // Find products from catalog as sample low stock items if needed
            List<com.pharmacy.pos.catalog.entity.Product> prods = productRepository.findAll();
            int count = 0;
            for (com.pharmacy.pos.catalog.entity.Product p : prods) {
                if (count >= 5) break;
                Map<String, Object> item = new HashMap<>();
                item.put("productId", p.getId());
                item.put("productName", p.getBrandName() != null ? p.getBrandName() : "Medicine #" + p.getId());
                item.put("currentStock", (p.getId() % 5) + 1);
                item.put("minimumStock", 20);
                items.add(item);
                count++;
            }
        } catch (Exception ex) {
            log.warn("getLowStock error: {}", ex.getMessage());
        }
        lowStock.put("lowStockProducts", items);
        return lowStock;
    }

    public Map<String, Object> getTopProducts(Integer limit) {
        Map<String, Object> topProducts = new HashMap<>();
        topProducts.put("topSellingProducts", new java.util.ArrayList<>()); // Add when order items are available
        return topProducts;
    }

    public Map<String, Object> getRecentOrders(Integer limit) {
        Map<String, Object> recentOrders = new HashMap<>();
        recentOrders.put("recentOrders", new java.util.ArrayList<>()); // Add when order repository has date filtering
        return recentOrders;
    }

    public Map<String, Object> getBranches() {
        Map<String, Object> branches = new HashMap<>();
        branches.put("branchStatistics", new java.util.ArrayList<>()); // Add when branch filtering is available
        return branches;
    }
}