package com.pharmacy.pos.sales.repository;

import com.pharmacy.pos.sales.entity.OrderReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderReturnItemRepository extends JpaRepository<OrderReturnItem, Long> {

    List<OrderReturnItem> findByOrderReturnId(Long orderReturnId);
}
