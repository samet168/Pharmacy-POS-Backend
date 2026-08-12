package com.pharmacy.pos.iam.entity;

import com.pharmacy.pos.branch.entity.Branch;
import com.pharmacy.pos.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_branches", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "branch_id"})
})
public class UserBranch extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
}
