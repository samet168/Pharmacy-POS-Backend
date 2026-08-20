package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT ub.branch.id FROM UserBranch ub WHERE ub.user.id = :userId")
    List<Long> findBranchIdsByUserId(@Param("userId") Long userId);

    boolean existsByUsername(String username);

    // organization is @ManyToOne — must use JPQL traversal, not derived query
    @Query("SELECT u FROM User u WHERE u.organization.id = :orgId")
    Page<User> findByOrganizationId(@Param("orgId") Long organizationId, Pageable pageable);

    // Used by AuthService pin-login — scan only within an organization for security
    @Query("SELECT u FROM User u WHERE u.organization.id = :orgId AND u.pinCode IS NOT NULL AND u.active = true")
    List<User> findActiveUsersWithPinByOrganizationId(@Param("orgId") Long organizationId);
}
