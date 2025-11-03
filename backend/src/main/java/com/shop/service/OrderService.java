package com.shop.service;

import com.shop.model.*;
import com.shop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createPendingOrder(Long userId) {
        List<CartItem> items = cartService.getActiveCartItems(userId);
        if (items.isEmpty()) throw new IllegalArgumentException("购物车为空");

        double total = 0.0;
        for (CartItem it : items) {
            Product p = productRepository.findById(it.getProductId()).orElseThrow(() -> new IllegalArgumentException("商品不存在"));
            if (p.getStock() < it.getQuantity()) throw new IllegalArgumentException("库存不足: " + p.getName());
            total += p.getPrice() * it.getQuantity();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("Pending");
        order.setTotalPrice(total);
        order = orderRepository.save(order);

        for (CartItem it : items) {
            Product p = productRepository.findById(it.getProductId()).orElseThrow();
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductId(p.getId());
            oi.setQuantity(it.getQuantity());
            oi.setPrice(p.getPrice());
            orderItemRepository.save(oi);
        }

        return order;
    }

    @Transactional
    public void markPaidAndFulfillCart(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        if (!order.getUserId().equals(userId)) throw new IllegalArgumentException("无权操作该订单");
        if (!"Pending".equals(order.getStatus())) throw new IllegalArgumentException("订单非待支付状态");

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            Product p = productRepository.findById(item.getProductId()).orElseThrow();
            int left = p.getStock() - item.getQuantity();
            if (left < 0) throw new IllegalArgumentException("库存不足: " + p.getName());
            p.setStock(left);
            productRepository.save(p);
        }

        order.setStatus("Paid");
        orderRepository.save(order);

        cartService.clearCart(userId);
    }

    public List<Order> listMyOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}


