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

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    private Long resolveCurrentUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) throw new IllegalArgumentException("未登录");
        User u = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return u.getId();
    }

    @PostMapping("/create")
    public ResponseDTO<Order> create(HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(orderService.createPendingOrder(userId));
    }

    @PostMapping("/pay")
    public ResponseDTO<Void> pay(HttpServletRequest request, @RequestBody Map<String, Object> req) {
        Long orderId = Long.valueOf(String.valueOf(req.get("orderId")));
        Long userId = resolveCurrentUserId(request);
        orderService.markPaidAndFulfillCart(orderId, userId);
        return ResponseDTO.ok(null);
    }

    @GetMapping
    public ResponseDTO<List<Order>> my(HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(orderService.listMyOrders(userId));
    }

    @GetMapping("/{id}")
    public ResponseDTO<Order> get(@PathVariable Long id, HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(orderService.getOrderById(id, userId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseDTO<Order> updateStatus(@PathVariable Long orderId, @RequestBody Map<String, String> req) {
        return ResponseDTO.ok(orderService.updateStatus(orderId, req.get("status")));
    }
}


