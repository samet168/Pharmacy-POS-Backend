package com.pharmacy.pos.purchasing.service;

import com.pharmacy.pos.common.enums.PurchaseStatus;
import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.catalog.entity.Supplier;
import com.pharmacy.pos.catalog.repository.SupplierRepository;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderItemRequest;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderRequest;
import com.pharmacy.pos.purchasing.dto.PurchaseOrderResponse;
import com.pharmacy.pos.purchasing.entity.PurchaseOrder;
import com.pharmacy.pos.purchasing.entity.PurchaseOrderItem;
import com.pharmacy.pos.purchasing.mapper.PurchaseOrderItemMapper;
import com.pharmacy.pos.purchasing.mapper.PurchaseOrderMapper;
import com.pharmacy.pos.purchasing.repository.PurchaseOrderItemRepository;
import com.pharmacy.pos.purchasing.repository.PurchaseOrderRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final SupplierRepository supplierRepository;

    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {
        if (purchaseOrderRepository.existsByPoNumber(request.getPoNumber())) {
            throw new DuplicateResourceException("Purchase Order with this PO number already exists");
        }

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));

        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(request);
        purchaseOrder.setOrganization(organization);
        purchaseOrder.setBranch(branch);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setStatus(PurchaseStatus.DRAFT);

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            Set<PurchaseOrderItem> items = new HashSet<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
                PurchaseOrderItem item = purchaseOrderItemMapper.toEntity(itemRequest);
                item.setPurchaseOrder(purchaseOrder);
                items.add(item);
                totalAmount = totalAmount.add(item.getSubtotal());
            }

            purchaseOrder.setItems(items);
            purchaseOrder.setTotalAmount(totalAmount);
        }

        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    @Transactional
    public PurchaseOrderResponse update(Long id, PurchaseOrderRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", id));

        if (!purchaseOrder.getPoNumber().equals(request.getPoNumber()) &&
            purchaseOrderRepository.existsByPoNumber(request.getPoNumber())) {
            throw new DuplicateResourceException("Purchase Order with this PO number already exists");
        }

        if (!purchaseOrder.getOrganization().getId().equals(request.getOrganizationId())) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));
            purchaseOrder.setOrganization(organization);
        }

        if (!purchaseOrder.getBranch().getId().equals(request.getBranchId())) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));
            purchaseOrder.setBranch(branch);
        }

        if (!purchaseOrder.getSupplier().getId().equals(request.getSupplierId())) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));
            purchaseOrder.setSupplier(supplier);
        }

        purchaseOrderMapper.updateEntityFromRequest(request, purchaseOrder);

        if (request.getItems() != null) {
            purchaseOrder.getItems().clear();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
                PurchaseOrderItem item = purchaseOrderItemMapper.toEntity(itemRequest);
                item.setPurchaseOrder(purchaseOrder);
                purchaseOrder.getItems().add(item);
                totalAmount = totalAmount.add(item.getSubtotal());
            }

            purchaseOrder.setTotalAmount(totalAmount);
        }

        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    @Transactional
    public PurchaseOrderResponse addItem(Long id, PurchaseOrderItemRequest itemRequest) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", id));

        if (purchaseOrder.getStatus() != PurchaseStatus.DRAFT) {
            throw new BusinessRuleException("Cannot add items to a purchase order that is not in DRAFT status");
        }

        PurchaseOrderItem item = purchaseOrderItemMapper.toEntity(itemRequest);
        item.setPurchaseOrder(purchaseOrder);
        purchaseOrder.getItems().add(item);

        BigDecimal currentTotal = purchaseOrder.getTotalAmount() != null ? purchaseOrder.getTotalAmount() : BigDecimal.ZERO;
        purchaseOrder.setTotalAmount(currentTotal.add(item.getSubtotal()));

        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    @Transactional
    public PurchaseOrderResponse submit(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", id));

        if (purchaseOrder.getStatus() != PurchaseStatus.DRAFT) {
            throw new BusinessRuleException("Purchase order is not in DRAFT status");
        }

        if (purchaseOrder.getItems() == null || purchaseOrder.getItems().isEmpty()) {
            throw new BusinessRuleException("Cannot submit a purchase order without items");
        }

        purchaseOrder.setStatus(PurchaseStatus.ORDERED);
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    @Transactional
    public PurchaseOrderResponse cancel(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", id));

        if (purchaseOrder.getStatus() == PurchaseStatus.RECEIVED) {
            throw new BusinessRuleException("Cannot cancel a purchase order that has been received");
        }

        purchaseOrder.setStatus(PurchaseStatus.CANCELLED);
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    public PurchaseOrderResponse getById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", id));
        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    public Page<PurchaseOrderResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return purchaseOrderRepository.findByOrganizationId(organizationId, pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    public Page<PurchaseOrderResponse> getByBranch(Long branchId, Pageable pageable) {
        return purchaseOrderRepository.findByBranchId(branchId, pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    public Page<PurchaseOrderResponse> getBySupplier(Long supplierId, Pageable pageable) {
        return purchaseOrderRepository.findBySupplierId(supplierId, pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    public Page<PurchaseOrderResponse> getByOrganizationAndStatus(Long organizationId, PurchaseStatus status, Pageable pageable) {
        return purchaseOrderRepository.findByOrganizationIdAndStatus(organizationId, status, pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    public Page<PurchaseOrderResponse> getAll(Pageable pageable) {
        return purchaseOrderRepository.findAll(pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", id));

        if (purchaseOrder.getStatus() != PurchaseStatus.DRAFT) {
            throw new BusinessRuleException("Cannot delete a purchase order that is not in DRAFT status");
        }

        purchaseOrderRepository.delete(purchaseOrder);
    }
}
