package com.pharmacy.pos.catalog.mapper;

import com.pharmacy.pos.catalog.dto.CategoryRequest;
import com.pharmacy.pos.catalog.dto.CategoryResponse;
import com.pharmacy.pos.catalog.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setNameKh(request.getNameKh());
        category.setActive(request.isActive());
        return category;
    }

    public CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setOrganizationId(category.getOrganization() != null ? category.getOrganization().getId() : null);
        response.setName(category.getName());
        response.setNameKh(category.getNameKh());
        response.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        response.setParentName(category.getParent() != null ? category.getParent().getName() : null);
        response.setActive(category.isActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }

    public void updateEntityFromRequest(Category category, CategoryRequest request) {
        category.setName(request.getName());
        category.setNameKh(request.getNameKh());
        category.setActive(request.isActive());
    }
}