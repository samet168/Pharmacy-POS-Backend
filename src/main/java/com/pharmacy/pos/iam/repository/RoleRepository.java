package com.pharmacy.pos.iam.repository;

import com.pharmacy.pos.iam.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT * FROM roles WHERE name = :name LIMIT 1", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);
    @Query(value = "SELECT * FROM roles WHERE LOWER(name) = LOWER(:name) LIMIT 1", nativeQuery = true)
    Optional<Role> findByNameIgnoreCase(@Param("name") String name);
    Optional<Role> findByNameAndSystemRole(String name, boolean systemRole);
    
    @Query("SELECT r FROM Role r WHERE r.organization.id = :organizationId OR r.organization IS NULL OR r.systemRole = true")
    Page<Role> findByOrganizationId(@Param("organizationId") Long organizationId, Pageable pageable);
}
