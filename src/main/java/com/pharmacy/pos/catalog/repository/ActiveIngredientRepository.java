package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveIngredientRepository extends JpaRepository<ActiveIngredient, Long> {

    @Query("SELECT a FROM ActiveIngredient a WHERE a.organization.id = :orgId")
    List<ActiveIngredient> findByOrganizationId(@Param("orgId") Long organizationId);
}
