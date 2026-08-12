package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByOrganizationId(Long organizationId);
    List<Category> findByParentId(Long parentId);
}
