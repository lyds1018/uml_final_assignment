package com.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.model.CartItem;
import com.shop.model.Order;
import com.shop.model.OrderItem;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createPendingOrder(Long userId) {
        List<CartItem> items = cartService.getActiveCartItems(userId);
        if (items.isEmpty()) throw new IllegalArgumentException("购物车为空");

        double total = 0.0;
        for (CartItem it : items) {
            Product p = productRepository.findById(it.getProductId()).orElseThrow();
            if (p.getStock() < it.getQuantity()) throw new IllegalArgumentException("库存不足: " + p.getName());
            total += p.getPrice() * it.getQuantity();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
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
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权操作该订单");
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException("订单非待支付状态");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        // 验证库存
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException(product.getName() + "库存不足");
            }
        }

        // 更新库存
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        // 更新订单状态
        order.setStatus("PAID");
        orderRepository.save(order);

        // 清空购物车
        cartService.clearCart(userId);
    }

    public List<Order> listMyOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权访问此订单");
        }

        // 加载订单项和相关的商品信息
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            item.setProduct(product);
        }
        order.setItems(items);

        return order;
    }

    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> listAllOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            for (OrderItem item : items) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                item.setProduct(product);
            }
            order.setItems(items);
            User u = userRepository.findById(order.getUserId()).orElse(null);
            order.setUser(u);
        }
        return orders;
    }
}


