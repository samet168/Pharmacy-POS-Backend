package com.pharmacy.pos.branch.repository;

import com.pharmacy.pos.branch.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceUuid(String deviceUuid);
    boolean existsByDeviceUuid(String deviceUuid);
    Page<Device> findAllByBranchId(Long branchId, Pageable pageable);
}
