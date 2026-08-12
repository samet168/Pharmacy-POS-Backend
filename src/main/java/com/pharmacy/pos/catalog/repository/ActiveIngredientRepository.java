package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.ActiveIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveIngredientRepository extends JpaRepository<ActiveIngredient, Long> {
}
