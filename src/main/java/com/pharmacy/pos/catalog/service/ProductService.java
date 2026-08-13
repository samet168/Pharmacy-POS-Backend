package com.pharmacy.pos.catalog.service;

import com.pharmacy.pos.common.exception.DuplicateResourceException;
import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.catalog.dto.ProductRequest;
import com.pharmacy.pos.catalog.dto.ProductResponse;
import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import com.pharmacy.pos.catalog.entity.Category;
import com.pharmacy.pos.catalog.entity.Product;
import com.pharmacy.pos.catalog.entity.Supplier;
import com.pharmacy.pos.catalog.mapper.ProductMapper;
import com.pharmacy.pos.catalog.repository.*;
import com.pharmacy.pos.service.CloudinaryService;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrganizationRepository organizationRepository;
    private final CategoryRepository categoryRepository;
    private final ActiveIngredientRepository activeIngredientRepository;
    private final SupplierRepository supplierRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", request.getOrganizationId()));

        if (productRepository.findByOrganizationIdAndSku(request.getOrganizationId(), request.getSku()).isPresent()) {
            throw new DuplicateResourceException("Product with this SKU already exists in this organization");
        }

        Product product = productMapper.toEntity(request);
        product.setOrganization(organization);

        if (request.getGenericNameId() != null) {
            ActiveIngredient ingredient = activeIngredientRepository.findById(request.getGenericNameId())
                    .orElseThrow(() -> new ResourceNotFoundException("ActiveIngredient", request.getGenericNameId()));
            product.setGenericNameId(ingredient);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getDefaultSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getDefaultSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getDefaultSupplierId()));
            product.setDefaultSupplier(supplier);
        }

        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createWithImage(ProductRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadProductImage(file);
        request.setImageUrl(imageUrl);
        return create(request);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (!product.getSku().equals(request.getSku()) &&
            productRepository.findByOrganizationIdAndSku(request.getOrganizationId(), request.getSku()).isPresent()) {
            throw new DuplicateResourceException("Product with this SKU already exists in this organization");
        }

        // Note: imageUrl is handled separately in updateWithImage method
        // We don't use the mapper for imageUrl to preserve existing values
        productMapper.updateEntityFromRequest(request, product);

        if (request.getGenericNameId() != null) {
            ActiveIngredient ingredient = activeIngredientRepository.findById(request.getGenericNameId())
                    .orElseThrow(() -> new ResourceNotFoundException("ActiveIngredient", request.getGenericNameId()));
            product.setGenericNameId(ingredient);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getDefaultSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getDefaultSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getDefaultSupplierId()));
            product.setDefaultSupplier(supplier);
        }

        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse updateWithImage(Long id, ProductRequest request, MultipartFile file) throws Exception {
        String imageUrl = cloudinaryService.uploadProductImage(file);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (!product.getSku().equals(request.getSku()) &&
            productRepository.findByOrganizationIdAndSku(request.getOrganizationId(), request.getSku()).isPresent()) {
            throw new DuplicateResourceException("Product with this SKU already exists in this organization");
        }

        if (request.getGenericNameId() != null) {
            ActiveIngredient ingredient = activeIngredientRepository.findById(request.getGenericNameId())
                    .orElseThrow(() -> new ResourceNotFoundException("ActiveIngredient", request.getGenericNameId()));
            product.setGenericNameId(ingredient);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getDefaultSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getDefaultSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getDefaultSupplierId()));
            product.setDefaultSupplier(supplier);
        }

        // Manually set imageUrl since mapper ignores it
        product.setImageUrl(imageUrl);
        
        // Use mapper for other fields (but not imageUrl)
        productMapper.updateEntityFromRequest(request, product);
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toResponse(product);
    }

    public Page<ProductResponse> getByOrganization(Long organizationId, Pageable pageable) {
        return productRepository.findByOrganizationId(organizationId, pageable)
                .map(productMapper::toResponse);
    }

    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        productRepository.delete(product);
    }
}
