package com.pharmacy.pos.branch.repository;

import com.pharmacy.pos.branch.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByDeviceUuid(String deviceUuid);

    boolean existsByDeviceUuid(String deviceUuid);

    // branch is @ManyToOne — derived query "findAllByBranchId" fails
    @Query("SELECT d FROM Device d WHERE d.branch.id = :branchId")
    Page<Device> findAllByBranchId(@Param("branchId") Long branchId, Pageable pageable);
}
