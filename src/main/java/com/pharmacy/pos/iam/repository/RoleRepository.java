package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByNameAndSystemRole(String name, boolean systemRole);
    Page<Role> findByOrganizationId(Long organizationId, Pageable pageable);
}
