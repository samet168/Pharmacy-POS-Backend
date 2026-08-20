package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.organization.id = :orgId")
    List<Category> findByOrganizationId(@Param("orgId") Long organizationId);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findByParentId(@Param("parentId") Long parentId);
}
