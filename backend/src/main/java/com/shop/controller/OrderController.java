package com.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.ResponseDTO;
import com.shop.model.Order;
import com.shop.model.User;
import com.shop.service.OrderService;
import com.shop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController                                      // REST 控制器，返回 JSON
@RequestMapping("/api/orders")                       // 所有接口路径前缀为 /api/orders
public class OrderController {
    private final OrderService orderService;         // 订单业务层
    private final UserService userService;           // 用户业务层，用于根据用户名查用户

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // 从 request 中提取当前登录用户 ID（JWT 过滤器预先把 username 放入 request attribute）
    private Long resolveCurrentUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");  // 登录态里的用户名

        // 根据用户名查完整的用户信息
        User u = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return u.getId();
    }

    // 创建一个待支付订单（通常用于从购物车生成订单）
    @PostMapping("/create")
    public ResponseDTO<Order> create(HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);                  // 获取当前用户 ID
        return ResponseDTO.ok(orderService.createPendingOrder(userId));
    }

    // 支付订单
    @PostMapping("/pay")
    public ResponseDTO<Void> pay(HttpServletRequest request, @RequestBody Map<String, Object> req) {
        // 从请求体中获取订单 ID
        Long orderId = Long.valueOf(String.valueOf(req.get("orderId")));
        Long userId = resolveCurrentUserId(request);                  // 当前用户
        orderService.markPaidAndFulfillCart(orderId, userId);        // 支付，清空购物车，扣减库存
        return ResponseDTO.ok(null);
    }

    // 查询当前用户所有订单
    @GetMapping
    public ResponseDTO<List<Order>> my(HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(orderService.listMyOrders(userId));
    }

    // 查询当前用户指定订单详情
    @GetMapping("/{id}")
    public ResponseDTO<Order> get(@PathVariable Long id, HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(orderService.getOrderById(id, userId)); // service 内会检查权限
    }

    // 用户将订单标记为已完成
    @PutMapping("/{orderId}/complete")
    public ResponseDTO<Order> completeOrder(@PathVariable Long orderId) {
        // 调用 service 将订单状态更新为 "COMPLETED"
        Order updatedOrder = orderService.updateStatus(orderId, "COMPLETED");
        return ResponseDTO.ok(updatedOrder);
    }
    
    // 用户将订单标记为已取消
    @PutMapping("/{orderId}/cancel")
    public ResponseDTO<Order> cancelOrder(@PathVariable Long orderId) {
        // 调用 service 将订单状态更新为 "CANCELLED"
        Order updatedOrder = orderService.updateStatus(orderId, "CANCELLED");
        return ResponseDTO.ok(updatedOrder);
    }
}



