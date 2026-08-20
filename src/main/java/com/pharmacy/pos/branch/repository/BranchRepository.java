package com.pharmacy.pos.branch.repository;

import com.pharmacy.pos.branch.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query("SELECT b FROM Branch b WHERE b.organization.id = :orgId")
    Page<Branch> findByOrganizationId(@Param("orgId") Long organizationId, Pageable pageable);

    boolean existsByCode(String code);
}
