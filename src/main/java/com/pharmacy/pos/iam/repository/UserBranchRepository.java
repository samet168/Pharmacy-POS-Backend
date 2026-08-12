package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.UserBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserBranchRepository extends JpaRepository<UserBranch, Long> {
    List<UserBranch> findByUserId(Long userId);

    @Query("SELECT ub.branch.id FROM UserBranch ub WHERE ub.user.id = :userId")
    List<Long> findBranchIdsByUserId(@Param("userId") Long userId);

    @Transactional
    void deleteByUserId(Long userId);
}
