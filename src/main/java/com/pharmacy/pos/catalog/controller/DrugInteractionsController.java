package com.pharmacy.pos.catalog.controller;

import com.pharmacy.pos.common.ApiResponse;
import com.pharmacy.pos.catalog.dto.DrugInteractionRequest;
import com.pharmacy.pos.catalog.dto.DrugInteractionResponse;
import com.pharmacy.pos.catalog.service.DrugInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drug-interactions")
@RequiredArgsConstructor
public class DrugInteractionsController {

    private final DrugInteractionService drugInteractionService;

    @PostMapping
    @Operation(summary = "Create drug interaction", description = "Create a new drug interaction")
    public ApiResponse<DrugInteractionResponse> create(@Valid @RequestBody DrugInteractionRequest request) {
        return ApiResponse.success(drugInteractionService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update drug interaction", description = "Update an existing drug interaction")
    public ApiResponse<DrugInteractionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DrugInteractionRequest request) {
        return ApiResponse.success(drugInteractionService.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get drug interaction by ID", description = "Retrieve a drug interaction by its ID")
    public ApiResponse<DrugInteractionResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(drugInteractionService.getById(id));
    }

    @GetMapping("/ingredient/{ingredientId}")
    @Operation(summary = "Get drug interactions by ingredient", description = "Retrieve all interactions for a specific ingredient")
    public ApiResponse<List<DrugInteractionResponse>> getByIngredient(@PathVariable Long ingredientId) {
        return ApiResponse.success(drugInteractionService.getByIngredient(ingredientId));
    }

    @GetMapping
    @Operation(summary = "Get all drug interactions", description = "Retrieve all drug interactions")
    public ApiResponse<List<DrugInteractionResponse>> getAll() {
        return ApiResponse.success(drugInteractionService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete drug interaction", description = "Delete a drug interaction by its ID")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        drugInteractionService.delete(id);
        return ApiResponse.success("Drug interaction deleted successfully", null);
    }
}
