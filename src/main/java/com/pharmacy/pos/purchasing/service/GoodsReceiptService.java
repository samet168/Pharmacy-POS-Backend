package com.pharmacy.pos.purchasing.service;

import com.pharmacy.pos.common.enums.MovementType;
import com.pharmacy.pos.common.enums.PurchaseStatus;
import com.pharmacy.pos.common.exception.BusinessRuleException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.branch.repository.BranchRepository;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.entity.ProductUnit;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.catalog.repository.ProductUnitRepository;
import com.pharmacy.pos.inventory.entity.BranchInventory;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import com.pharmacy.pos.inventory.entity.StockMovement;
import com.pharmacy.pos.inventory.repository.BranchInventoryRepository;
import com.pharmacy.pos.inventory.repository.ProductBatchRepository;
import com.pharmacy.pos.inventory.repository.StockMovementRepository;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptItemRequest;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptRequest;
import com.pharmacy.pos.purchasing.dto.GoodsReceiptResponse;
import com.pharmacy.pos.purchasing.entity.GoodsReceipt;
import com.pharmacy.pos.purchasing.entity.GoodsReceiptItem;
import com.pharmacy.pos.purchasing.entity.PurchaseOrder;
import com.pharmacy.pos.purchasing.entity.PurchaseOrderItem;
import com.pharmacy.pos.purchasing.mapper.GoodsReceiptItemMapper;
import com.pharmacy.pos.purchasing.mapper.GoodsReceiptMapper;
import com.pharmacy.pos.purchasing.repository.GoodsReceiptItemRepository;
import com.pharmacy.pos.purchasing.repository.GoodsReceiptRepository;
import com.pharmacy.pos.purchasing.repository.PurchaseOrderItemRepository;
import com.pharmacy.pos.purchasing.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
    private final GoodsReceiptMapper goodsReceiptMapper;
    private final GoodsReceiptItemMapper goodsReceiptItemMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductBatchRepository productBatchRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional
    public GoodsReceiptResponse create(GoodsReceiptRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", request.getPurchaseOrderId()));

        if (purchaseOrder.getStatus() != PurchaseStatus.ORDERED) {
            throw new BusinessRuleException("Cannot create goods receipt for a purchase order that is not in ORDERED status");
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        GoodsReceipt goodsReceipt = goodsReceiptMapper.toEntity(request);
        goodsReceipt.setPurchaseOrder(purchaseOrder);
        goodsReceipt.setBranch(branch);
        goodsReceipt.setReceivedAt(LocalDateTime.now());

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            Set<GoodsReceiptItem> items = new HashSet<>();

            for (GoodsReceiptItemRequest itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.getProductId()));

                ProductBatch batch = productBatchRepository.findById(itemRequest.getBatchId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product Batch", itemRequest.getBatchId()));

                ProductUnit unit = productUnitRepository.findById(itemRequest.getUnitId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product Unit", itemRequest.getUnitId()));

                GoodsReceiptItem item = goodsReceiptItemMapper.toEntity(itemRequest);
                item.setGoodsReceipt(goodsReceipt);
                item.setProduct(product);
                item.setBatch(batch);
                item.setUnit(unit);
                items.add(item);

                // Update branch inventory
                BranchInventory inventory = branchInventoryRepository
                        .findByBranchIdAndBatchId(branch.getId(), batch.getId())
                        .orElse(new BranchInventory());

                if (inventory.getId() == null) {
                    inventory.setBranch(branch);
                    inventory.setBatch(batch);
                    inventory.setQuantityInBaseUnit(itemRequest.getQuantity());
                } else {
                    inventory.setQuantityInBaseUnit(inventory.getQuantityInBaseUnit() + itemRequest.getQuantity());
                }
                branchInventoryRepository.save(inventory);

                // Create stock movement
                StockMovement stockMovement = new StockMovement();
                stockMovement.setBranch(branch);
                stockMovement.setBatch(batch);
                stockMovement.setMovementType(MovementType.PURCHASE_IN);
                stockMovement.setQuantityInBaseUnit(itemRequest.getQuantity());
                stockMovement.setReferenceTable("goods_receipts");
                stockMovement.setReferenceId(goodsReceipt.getId());
                stockMovement.setPerformedBy(request.getReceivedBy());
                stockMovementRepository.save(stockMovement);
            }

            goodsReceipt.setItems(items);
        }

        goodsReceipt = goodsReceiptRepository.save(goodsReceipt);

        // Update purchase order status based on received quantities
        updatePurchaseOrderStatus(purchaseOrder);

        return goodsReceiptMapper.toResponse(goodsReceipt);
    }

    private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {
        Map<Long, Integer> orderedQuantities = purchaseOrder.getItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        PurchaseOrderItem::getQuantity
                ));

        Map<Long, Integer> receivedQuantities = goodsReceiptRepository
                .findByPurchaseOrderId(purchaseOrder.getId(), null)
                .getContent().stream()
                .flatMap(receipt -> receipt.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingInt(GoodsReceiptItem::getQuantity)
                ));

        boolean allReceived = orderedQuantities.entrySet().stream()
                .allMatch(entry -> {
                    Integer received = receivedQuantities.getOrDefault(entry.getKey(), 0);
                    return received >= entry.getValue();
                });

        boolean partiallyReceived = orderedQuantities.entrySet().stream()
                .anyMatch(entry -> {
                    Integer received = receivedQuantities.getOrDefault(entry.getKey(), 0);
                    return received > 0 && received < entry.getValue();
                });

        if (allReceived) {
            purchaseOrder.setStatus(PurchaseStatus.RECEIVED);
        } else if (partiallyReceived) {
            purchaseOrder.setStatus(PurchaseStatus.PARTIALLY_RECEIVED);
        }

        purchaseOrderRepository.save(purchaseOrder);
    }

    public GoodsReceiptResponse getById(Long id) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt", id));
        return goodsReceiptMapper.toResponse(goodsReceipt);
    }

    @Transactional
    public GoodsReceiptResponse update(Long id, GoodsReceiptRequest request) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt", id));

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", request.getPurchaseOrderId()));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        goodsReceiptMapper.updateEntityFromRequest(request, goodsReceipt);
        goodsReceipt.setPurchaseOrder(purchaseOrder);
        goodsReceipt.setBranch(branch);

        goodsReceipt = goodsReceiptRepository.save(goodsReceipt);
        return goodsReceiptMapper.toResponse(goodsReceipt);
    }

    public Page<GoodsReceiptResponse> getByPurchaseOrder(Long purchaseOrderId, Pageable pageable) {
        return goodsReceiptRepository.findByPurchaseOrderId(purchaseOrderId, pageable)
                .map(goodsReceiptMapper::toResponse);
    }

    public Page<GoodsReceiptResponse> getByBranch(Long branchId, Pageable pageable) {
        return goodsReceiptRepository.findByBranchId(branchId, pageable)
                .map(goodsReceiptMapper::toResponse);
    }

    public Page<GoodsReceiptResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return goodsReceiptRepository.findByBranchOrganizationId(organizationId, pageable)
                .map(goodsReceiptMapper::toResponse);
    }

    public Page<GoodsReceiptResponse> getAll(Pageable pageable) {
        return goodsReceiptRepository.findAll(pageable)
                .map(goodsReceiptMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt", id));

        // Revert stock movements
        for (GoodsReceiptItem item : goodsReceipt.getItems()) {
            BranchInventory inventory = branchInventoryRepository
                    .findByBranchIdAndBatchId(goodsReceipt.getBranch().getId(), item.getBatch().getId())
                    .orElse(null);

            if (inventory != null) {
                inventory.setQuantityInBaseUnit(Math.max(0, inventory.getQuantityInBaseUnit() - item.getQuantity()));
                branchInventoryRepository.save(inventory);
            }
        }

        goodsReceiptRepository.delete(goodsReceipt);

        // Re-evaluate purchase order status
        updatePurchaseOrderStatus(goodsReceipt.getPurchaseOrder());
    }
}
