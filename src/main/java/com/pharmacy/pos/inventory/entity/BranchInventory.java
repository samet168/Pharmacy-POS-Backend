package com.pharmacy.pos.inventory.entity;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.common.TimestampEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "branch_inventories", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"branch_id", "batch_id"})
})
public class BranchInventory extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private ProductBatch batch;

    @Column(name = "quantity_in_base_unit", nullable = false)
    private int quantityInBaseUnit = 0;
}
