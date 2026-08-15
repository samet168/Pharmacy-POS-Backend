package com.pharmacy.pos.catalog.repository;

import com.pharmacy.pos.catalog.entity.DrugInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, Long> {
    
    @Query("SELECT di FROM DrugInteraction di WHERE di.ingredientA.id = :ingredientId OR di.ingredientB.id = :ingredientId")
    List<DrugInteraction> findByIngredientId(@Param("ingredientId") Long ingredientId);
    
    List<DrugInteraction> findByIngredientAIdOrIngredientBId(Long ingredientAId, Long ingredientBId);
}
