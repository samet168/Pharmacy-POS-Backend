package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByUserIdAndStatus(Long userId, com.pharmacy.pos.common.enums.ShiftStatus status);
    Page<Shift> findByBranchIdOrderByOpenedAtDesc(Long branchId, Pageable pageable);
    Page<Shift> findByUserIdOrderByOpenedAtDesc(Long userId, Pageable pageable);
}
