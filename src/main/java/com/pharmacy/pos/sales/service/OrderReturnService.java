package com.pharmacy.pos.sales.service;

import com.pharmacy.pos.common.exception.ResourceNotFoundException;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.sales.dto.OrderReturnItemRequest;
import com.pharmacy.pos.sales.dto.OrderReturnRequest;
import com.pharmacy.pos.sales.dto.OrderReturnResponse;
import com.pharmacy.pos.sales.entity.Order;
import com.pharmacy.pos.sales.entity.OrderItem;
import com.pharmacy.pos.sales.entity.OrderReturn;
import com.pharmacy.pos.sales.entity.OrderReturnItem;
import com.pharmacy.pos.sales.mapper.OrderReturnItemMapper;
import com.pharmacy.pos.sales.mapper.OrderReturnMapper;
import com.pharmacy.pos.sales.repository.OrderItemRepository;
import com.pharmacy.pos.sales.repository.OrderRepository;
import com.pharmacy.pos.sales.repository.OrderReturnItemRepository;
import com.pharmacy.pos.sales.repository.OrderReturnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderReturnService {

    private final OrderReturnRepository orderReturnRepository;
    private final OrderReturnItemRepository orderReturnItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderReturnMapper orderReturnMapper;
    private final OrderReturnItemMapper orderReturnItemMapper;
    private final UserRepository userRepository;

    @Transactional
    public OrderReturnResponse processReturn(OrderReturnRequest request) {
        log.info("Processing return for order: {}", request.getOrderId());

        // Validate order exists
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.getOrderId()));

        // Create order return
        OrderReturn orderReturn = orderReturnMapper.toEntity(request);
        orderReturn.setOrder(order);
        // Set processedBy user separately since mapper can't handle Long -> User mapping
        if (request.getProcessedBy() != null) {
            User processedByUser = userRepository.findById(request.getProcessedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", request.getProcessedBy()));
            orderReturn.setProcessedBy(processedByUser);
        }
        orderReturn = orderReturnRepository.save(orderReturn);

        // Process return items
        List<OrderReturnItem> returnItems = new ArrayList<>();
        for (OrderReturnItemRequest itemRequest : request.getItems()) {
            OrderReturnItem returnItem = processReturnItem(orderReturn, itemRequest, order);
            returnItems.add(returnItem);
        }

        returnItems = orderReturnItemRepository.saveAll(returnItems);

        log.info("Return processed successfully for order return: {}", orderReturn.getId());
        
        OrderReturnResponse response = orderReturnMapper.toResponse(orderReturn);
        if (orderReturn.getProcessedBy() != null) {
            response.setProcessedBy(orderReturn.getProcessedBy().getId());
        }
        return response;
    }

    private OrderReturnItem processReturnItem(OrderReturn orderReturn, OrderReturnItemRequest itemRequest, Order order) {
        // Validate order item exists
        OrderItem orderItem = orderItemRepository.findById(itemRequest.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem", itemRequest.getOrderItemId()));

        // Create return item
        OrderReturnItem returnItem = orderReturnItemMapper.toEntity(itemRequest);
        returnItem.setOrderReturn(orderReturn);
        returnItem.setOrderItem(orderItem);

        // Handle restock if requested
        if (Boolean.TRUE.equals(itemRequest.getRestock())) {
            // TODO: Implement proper restock with entity relationships
            // restockItem(order, orderItem, itemRequest.getQuantity());
            log.info("TODO: Restock {} units for item: {}", itemRequest.getQuantity(), itemRequest.getOrderItemId());
        }

        return returnItem;
    }

    /*
    private void restockItem(Order order, OrderItem orderItem, Integer quantity) {
        // TODO: Implement proper restock with entity relationships
        // For now, stub this to get compilation working
        log.info("TODO: Restock {} units for order: {}, item: {}", quantity, order.getId(), orderItem.getId());
    }
    */

    public OrderReturnResponse getById(Long id) {
        OrderReturn orderReturn = orderReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderReturn", id));
        OrderReturnResponse response = orderReturnMapper.toResponse(orderReturn);
        if (orderReturn.getProcessedBy() != null) {
            response.setProcessedBy(orderReturn.getProcessedBy().getId());
        }
        return response;
    }

    public List<OrderReturnResponse> getByOrderId(Long orderId) {
        List<OrderReturn> returns = orderReturnRepository.findByOrderId(orderId);
        return orderReturnMapper.toResponseList(returns);
    }
}
