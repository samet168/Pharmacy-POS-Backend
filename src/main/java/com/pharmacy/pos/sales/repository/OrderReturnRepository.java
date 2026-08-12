package com.pharmacy.pos.sales.repository;

import com.pharmacy.pos.sales.entity.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {

    List<OrderReturn> findByOrderId(Long orderId);
}
