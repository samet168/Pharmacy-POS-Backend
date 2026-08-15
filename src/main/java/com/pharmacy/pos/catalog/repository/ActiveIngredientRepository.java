package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveIngredientRepository extends JpaRepository<ActiveIngredient, Long> {
    List<ActiveIngredient> findByOrganizationId(Long organizationId);
}
