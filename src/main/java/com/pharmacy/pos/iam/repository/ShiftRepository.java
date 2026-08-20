package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.common.enums.ShiftStatus;
import com.pharmacy.pos.iam.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // user and branch are @ManyToOne — derived queries on plain "userId"/"branchId" fail
    @Query("SELECT s FROM Shift s WHERE s.user.id = :userId AND s.status = :status")
    Optional<Shift> findByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") ShiftStatus status);

    @Query("SELECT s FROM Shift s WHERE s.branch.id = :branchId ORDER BY s.openedAt DESC")
    Page<Shift> findByBranchIdOrderByOpenedAtDesc(
            @Param("branchId") Long branchId,
            Pageable pageable);

    @Query("SELECT s FROM Shift s WHERE s.user.id = :userId ORDER BY s.openedAt DESC")
    Page<Shift> findByUserIdOrderByOpenedAtDesc(
            @Param("userId") Long userId,
            Pageable pageable);
}
