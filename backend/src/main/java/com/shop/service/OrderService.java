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
    private final OrderRepository orderRepository;           // 订单表访问
    private final OrderItemRepository orderItemRepository;   // 订单项表访问
    private final CartService cartService;                   // 购物车操作
    private final ProductRepository productRepository;       // 商品表访问
    private final UserRepository userRepository;             // 用户表访问

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartService cartService,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // 创建一个待支付订单（根据用户的购物车）
    @Transactional
    public Order createPendingOrder(Long userId) {
        // 查询购物车所有未结算商品
        List<CartItem> items = cartService.getActiveCartItems(userId);
        if (items.isEmpty()) throw new IllegalArgumentException("购物车为空");

        // 计算总价格，同时验证库存
        double total = 0.0;
        for (CartItem it : items) {
            Product p = productRepository.findById(it.getProductId()).orElseThrow();
            if (p.getStock() < it.getQuantity()) {
                throw new IllegalArgumentException(p.getName() + "库存不足");
            }
            total += p.getPrice() * it.getQuantity();
        }

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");              // 新订单默认“待支付”
        order.setTotalPrice(total);
        order = orderRepository.save(order);     // 保存订单主表

        // 创建订单项（从购物车复制过来）
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

    // 支付订单并扣减库存，完成购物车结算
    @Transactional
    public void markPaidAndFulfillCart(Long orderId, Long userId) {
        // 检查订单存在性和归属
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权操作该订单");
        }

        // 必须是待支付状态才能支付
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException("订单非待支付状态");
        }

        // 查询订单项
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        // 检查库存是否足够
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException(product.getName() + "库存不足");
            }
        }

        // 扣减库存
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        // 更新订单状态为已支付
        order.setStatus("PAID");
        orderRepository.save(order);

        // 清空购物车（支付后清除）
        cartService.clearCart(userId);
    }

    // 查询当前用户的全部订单
    public List<Order> listMyOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // 查询单个订单详情（包含订单项和商品信息）
    public Order getOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));

        // 权限校验：用户只能查看自己的订单
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权访问此订单");
        }

        // 查询订单项并附带商品信息
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            item.setProduct(product);
        }
        order.setItems(items);

        return order;
    }

    // 更新订单状态（管理员和用户共用）
    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));

        order.setStatus(status);                 // 直接修改状态
        return orderRepository.save(order);
    }

    // 后台管理员：列出全部订单（订单信息 + 用户信息）
    public List<Order> listAllOrders() {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            // 加载用户信息
            User u = userRepository.findById(order.getUserId()).orElse(null);
            order.setUser(u);

            // 不加载订单项（保持为空或保持默认）
            order.setItems(null);
        }

        return orders;
    }
}
