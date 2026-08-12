package com.pharmacy.pos.inventory.entity;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.common.BaseEntity;
import com.pharmacy.pos.common.enums.MovementType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_movements")
public class StockMovement extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private ProductBatch batch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType movementType;

    @Column(name = "quantity_in_base_unit", nullable = false)
    private int quantityInBaseUnit;

    @Column(name = "reference_table")
    private String referenceTable;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "performed_by")
    private Long performedBy;
}
