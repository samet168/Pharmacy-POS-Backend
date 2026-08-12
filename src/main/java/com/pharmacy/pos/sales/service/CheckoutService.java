package com.pharmacy.pos.sales.service;

import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.common.enums.MovementType;
import com.pharmacy.pos.customer.service.PrescriptionService;
import com.pharmacy.pos.inventory.entity.BranchInventory;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import com.pharmacy.pos.inventory.entity.StockMovement;
import com.pharmacy.pos.inventory.repository.BranchInventoryRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.inventory.repository.StockMovementRepository;
import com.pharmacy.pos.sales.dto.*;
import com.pharmacy.pos.sales.entity.Order;
import com.pharmacy.pos.sales.entity.OrderItem;
import com.pharmacy.pos.sales.entity.Payment;
import com.pharmacy.pos.sales.mapper.OrderItemMapper;
import com.pharmacy.pos.sales.mapper.OrderMapper;
import com.pharmacy.pos.sales.mapper.PaymentMapper;
import com.pharmacy.pos.sales.repository.OrderItemRepository;
import com.pharmacy.pos.sales.repository.OrderRepository;
import com.pharmacy.pos.sales.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;
    private final ProductRepository productRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final ProductBatchRepository productBatchRepository;
    private final StockMovementRepository stockMovementRepository;
    private final PrescriptionService prescriptionService;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        log.info("Processing checkout for organization: {}, branch: {}", 
                request.getOrganizationId(), request.getBranchId());

        // Validate and check prescriptions
        validatePrescriptionRequirements(request);

        // Check for allergies if customer is provided
        List<String> allergyWarnings = new ArrayList<>();
        if (request.getCustomerId() != null) {
            allergyWarnings = checkCustomerAllergies(request);
        }

        // Calculate totals
        BigDecimal subtotal = calculateSubtotal(request.getItems());
        BigDecimal discountAmount = BigDecimal.ZERO; // TODO: Apply promotion discount
        BigDecimal taxAmount = BigDecimal.ZERO; // TODO: Calculate tax
        BigDecimal grandTotal = subtotal.subtract(discountAmount).add(taxAmount);

        // Generate invoice number if not provided
        String invoiceNumber = request.getInvoiceNumber();
        if (invoiceNumber == null) {
            invoiceNumber = generateInvoiceNumber(request.getBranchId());
        }

        // Generate client UUID if not provided
        String clientUuid = request.getClientUuid();
        if (clientUuid == null) {
            clientUuid = UUID.randomUUID().toString();
        }

        // Create order
        Order order = createOrder(request, invoiceNumber, clientUuid, subtotal, discountAmount, taxAmount, grandTotal);
        order = orderRepository.save(order);

        // Process order items with FEFO allocation
        List<OrderItem> orderItems = processOrderItems(request, order);
        orderItems = orderItemRepository.saveAll(orderItems);

        // Create payments
        List<Payment> payments = processPayments(request, order);
        payments = paymentRepository.saveAll(payments);

        // TODO: Create promotion_usages if promotionId is provided
        // TODO: Create loyalty_transactions if loyaltyPointsEarned is provided
        // TODO: Handle controlled substance rules

        log.info("Checkout completed successfully for order: {}", order.getId());

        return buildCheckoutResponse(order, orderItems, payments, allergyWarnings);
    }

    private void validatePrescriptionRequirements(CheckoutRequest request) {
        for (CheckoutItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", item.getProductId()));

            if (product.isRequiresPrescription() && request.getPrescriptionId() == null) {
                throw new BusinessRuleException(
                        "Product " + product.getSku() + " requires a prescription");
            }

            // TODO: Add controlled substance validation
            if (product.isControlledSubstance()) {
                // Stubbed business rule - to be implemented later
                log.warn("Controlled substance validation needed for product: {}", product.getSku());
            }
        }
    }

    private List<String> checkCustomerAllergies(CheckoutRequest request) {
        List<Long> productIds = request.getItems().stream()
                .map(CheckoutItemRequest::getProductId)
                .toList();

        var allergyMatches = prescriptionService.checkProductAllergies(
                request.getCustomerId(), productIds);

        List<String> warnings = new ArrayList<>();
        for (var match : allergyMatches) {
            warnings.add(String.format("Product %s (%s) contains ingredient: %s", 
                    match.productSku(), match.productName(), match.ingredientName()));
        }

        return warnings;
    }

    private BigDecimal calculateSubtotal(List<CheckoutItemRequest> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateInvoiceNumber(Long branchId) {
        // Simple invoice number generation - can be enhanced later
        return "INV-" + branchId + "-" + System.currentTimeMillis();
    }

    private Order createOrder(CheckoutRequest request, String invoiceNumber, String clientUuid,
                              BigDecimal subtotal, BigDecimal discountAmount, BigDecimal taxAmount, BigDecimal grandTotal) {
        Order order = new Order();
        order.setClientUuid(clientUuid);
        order.setInvoiceNumber(invoiceNumber);
        // TODO: Load and set relationship entities (Organization, Branch, Device, User, Customer, Shift, Prescription)
        // For now, we stub this to get compilation working
        order.setSubtotal(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setTaxAmount(taxAmount);
        order.setGrandTotal(grandTotal);
        order.setCreatedAtDevice(java.time.LocalDateTime.now());
        return order;
    }

    private List<OrderItem> processOrderItems(CheckoutRequest request, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CheckoutItemRequest itemRequest : request.getItems()) {
            // TODO: FEFO allocation - consume oldest expiry batches first
            // Commented out for now to fix runtime query issue
            // List<OrderItem> itemsForRequest = allocateStockFEFO(
            //         order, itemRequest.getProductId(), itemRequest.getQuantity(),
            //         itemRequest.getUnitId(), itemRequest.getUnitPrice(), itemRequest.getDosageInstruction());
            // orderItems.addAll(itemsForRequest);
            
            // Stub implementation
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(itemRequest.getUnitPrice());
            orderItem.setSubtotal(itemRequest.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            orderItem.setDosageInstruction(itemRequest.getDosageInstruction());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private List<OrderItem> allocateStockFEFO(Order order, Long productId, Integer requestedQuantity,
                                              Long unitId, BigDecimal unitPrice, String dosageInstruction) {
        // TODO: Implement proper FEFO allocation with entity relationships
        // For now, stub this to get runtime working
        List<OrderItem> orderItems = new ArrayList<>();
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        // TODO: Set product, batch, unit entities
        orderItem.setQuantity(requestedQuantity);
        orderItem.setUnitPrice(unitPrice);
        orderItem.setSubtotal(unitPrice.multiply(BigDecimal.valueOf(requestedQuantity)));
        orderItem.setDosageInstruction(dosageInstruction);
        orderItems.add(orderItem);
        
        return orderItems;
    }

    private void createStockMovement(Long branchId, Long batchId, MovementType movementType,
                                      Integer quantity, Long referenceOrderId) {
        // TODO: Implement proper stock movement with entity relationships
        // For now, stub this to get compilation working
        log.info("TODO: Create stock movement for branch: {}, batch: {}, type: {}, quantity: {}", 
                branchId, batchId, movementType, quantity);
    }

    private List<Payment> processPayments(CheckoutRequest request, Order order) {
        List<Payment> payments = new ArrayList<>();

        for (PaymentRequest paymentRequest : request.getPayments()) {
            Payment payment = paymentMapper.toEntity(paymentRequest);
            payment.setOrder(order);
            payments.add(payment);
        }

        return payments;
    }

    private CheckoutResponse buildCheckoutResponse(Order order, List<OrderItem> orderItems,
                                                    List<Payment> payments, List<String> allergyWarnings) {
        CheckoutResponse response = new CheckoutResponse();
        response.setOrder(orderMapper.toResponse(order));
        response.setItems(orderItemMapper.toResponseList(orderItems));
        response.setPayments(paymentMapper.toResponseList(payments));
        response.setAllergyWarnings(allergyWarnings);
        response.setMessage("Checkout completed successfully");
        return response;
    }
}
