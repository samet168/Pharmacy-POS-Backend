package com.pharmacy.pos.sales.service;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.entity.Device;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.branch.repository.DeviceRepository;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.entity.ProductUnit;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.catalog.repository.ProductUnitRepository;
import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.common.enums.MovementType;
import com.pharmacy.pos.customer.entity.Customer;
import com.pharmacy.pos.customer.entity.Prescription;
import com.pharmacy.pos.customer.repository.CustomerRepository;
import com.pharmacy.pos.customer.repository.PrescriptionRepository;
import com.pharmacy.pos.customer.service.PrescriptionService;
import com.pharmacy.pos.iam.entity.Shift;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.repository.ShiftRepository;
import com.pharmacy.pos.iam.repository.UserRepository;
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
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final ProductUnitRepository productUnitRepository;
    private final ProductBatchRepository productBatchRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final PrescriptionService prescriptionService;
    private final BranchRepository branchRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final CustomerRepository customerRepository;
    private final ShiftRepository shiftRepository;
    private final PrescriptionRepository prescriptionRepository;

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
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
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

        List<PrescriptionService.AllergyMatch> allergyMatches = prescriptionService.checkProductAllergies(
                request.getCustomerId(), productIds);

        List<String> warnings = new ArrayList<>();
        for (PrescriptionService.AllergyMatch match : allergyMatches) {
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

        // ── Required relationships ──────────────────────────────────────────
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
        order.setOrganization(organization);

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));
        order.setBranch(branch);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        order.setUser(user);

        // ── Optional relationships ──────────────────────────────────────────
        if (request.getDeviceId() != null) {
            Device device = deviceRepository.findById(request.getDeviceId())
                    .orElse(null); // device not strictly required — log if missing
            if (device != null) {
                order.setDevice(device);
            } else {
                log.warn("Device ID {} not found, proceeding without device", request.getDeviceId());
            }
        }

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElse(null);
            if (customer != null) {
                order.setCustomer(customer);
            } else {
                log.warn("Customer ID {} not found, proceeding as walk-in", request.getCustomerId());
            }
        }

        if (request.getShiftId() != null) {
            Shift shift = shiftRepository.findById(request.getShiftId())
                    .orElse(null);
            if (shift != null) {
                order.setShift(shift);
            } else {
                log.warn("Shift ID {} not found, proceeding without shift", request.getShiftId());
            }
        }

        if (request.getPrescriptionId() != null) {
            Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                    .orElse(null);
            if (prescription != null) {
                order.setPrescription(prescription);
            } else {
                log.warn("Prescription ID {} not found, proceeding without prescription", request.getPrescriptionId());
            }
        }

        order.setPrescriptionUrl(request.getPrescriptionUrl());
        order.setSubtotal(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setTaxAmount(taxAmount);
        order.setGrandTotal(grandTotal);
        order.setCreatedAtDevice(LocalDateTime.now());

        return order;
    }

    private List<OrderItem> processOrderItems(CheckoutRequest request, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CheckoutItemRequest itemRequest : request.getItems()) {
            // Load product entity
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.getProductId()));

            // Load unit — try the requested unitId first, then fall back to the
            // product's own base unit, then any unit belonging to the product
            ProductUnit unit = resolveProductUnit(product, itemRequest.getUnitId());

            // Load batch — use FEFO: first expiry first
            List<ProductBatch> batches = productBatchRepository.findByProductIdOrderByExpiryDateAsc(product.getId());
            ProductBatch batch = batches.isEmpty() ? null : batches.get(0);

            if (batch == null) {
                // Auto-create a default batch for products with no batch configured
                // This ensures the order_items.batch_id FK never violates NOT NULL
                ProductBatch defaultBatch = new ProductBatch();
                defaultBatch.setProduct(product);
                defaultBatch.setBatchNumber("AUTO-" + product.getSku() + "-" + System.currentTimeMillis());
                defaultBatch.setExpiryDate(LocalDate.now().plusYears(5)); // far-future expiry
                batch = productBatchRepository.save(defaultBatch);
                log.warn("Auto-created default batch {} for product {} (id={})",
                        batch.getBatchNumber(), product.getSku(), product.getId());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setUnit(unit);
            orderItem.setBatch(batch);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(itemRequest.getUnitPrice());
            orderItem.setSubtotal(itemRequest.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            orderItem.setDosageInstruction(itemRequest.getDosageInstruction());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    /**
     * Resolve the ProductUnit for an order item.
     * Priority:
     *   1. The exact unitId sent by the client (if it belongs to this product)
     *   2. The product's base unit
     *   3. The first unit associated with the product
     *   4. Any ProductUnit by the given id (cross-product fallback)
     */
    private ProductUnit resolveProductUnit(Product product, Long requestedUnitId) {
        List<ProductUnit> productUnits = productUnitRepository.findByProductId(product.getId());

        // 1. Try exact match within this product's units (only if requestedUnitId is a real ID > 0)
        if (requestedUnitId != null && requestedUnitId > 0) {
            for (ProductUnit pu : productUnits) {
                if (pu.getId().equals(requestedUnitId)) {
                    return pu;
                }
            }
        }

        // 2. Fall back to base unit of this product
        for (ProductUnit pu : productUnits) {
            if (pu.isBaseUnit()) {
                log.warn("Requested unitId {} not found for product {}, using base unit {}",
                        requestedUnitId, product.getSku(), pu.getId());
                return pu;
            }
        }

        // 3. Fall back to first available unit for this product
        if (!productUnits.isEmpty()) {
            ProductUnit fallback = productUnits.get(0);
            log.warn("Requested unitId {} not found for product {}, using first unit {}",
                    requestedUnitId, product.getSku(), fallback.getId());
            return fallback;
        }

        // 4. Last resort: look up the requested ID globally (only if requestedUnitId is a real ID > 0)
        if (requestedUnitId != null && requestedUnitId > 0) {
            return productUnitRepository.findById(requestedUnitId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "ProductUnit (no units exist for product " + product.getSku() + ")",
                            requestedUnitId));
        }

        // 5. Auto-create a default "pcs" unit for products with no units configured
        //    This ensures checkout works for any product even when units haven't been set up yet.
        ProductUnit defaultUnit = new ProductUnit();
        defaultUnit.setProduct(product);
        defaultUnit.setUnitName("pcs");
        defaultUnit.setConversionFactor(1);
        defaultUnit.setBaseUnit(true);
        defaultUnit.setSellingPrice(BigDecimal.ZERO);
        defaultUnit.setCostPrice(BigDecimal.ZERO);
        ProductUnit savedUnit = productUnitRepository.save(defaultUnit);
        log.warn("Auto-created default unit 'pcs' (id={}) for product {} (id={})",
                savedUnit.getId(), product.getSku(), product.getId());
        return savedUnit;
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
