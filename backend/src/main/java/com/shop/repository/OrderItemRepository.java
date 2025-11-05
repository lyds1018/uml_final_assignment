package com.shop.repository;

import com.shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 订单ID匹配查询订单项
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}


