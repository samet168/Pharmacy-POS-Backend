package com.pharmacy.pos.inventory.mapper;

import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.repository.ProductRepository;
import com.pharmacy.pos.inventory.dto.ProductBatchRequest;
import com.pharmacy.pos.inventory.entity.ProductBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductBatchMapper {

    private final ProductRepository productRepository;

    public ProductBatch toEntity(ProductBatchRequest request) {
        ProductBatch batch = new ProductBatch();
        updateEntityFromRequest(request, batch);
        return batch;
    }

    public void updateEntityFromRequest(ProductBatchRequest request, ProductBatch batch) {
        batch.setBatchNumber(request.getBatchNumber());
        batch.setMfgDate(request.getMfgDate());
        batch.setExpiryDate(request.getExpiryDate());
        
        // Safe null check for product
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElse(null);
            batch.setProduct(product);
        }
    }
}