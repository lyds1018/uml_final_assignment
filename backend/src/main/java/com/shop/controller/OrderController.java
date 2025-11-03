package com.shop.controller;

import com.shop.dto.ResponseDTO;
import com.shop.model.Order;
import com.shop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseDTO<Order> create(@RequestBody Map<String, Object> req) {
        Long userId = Long.valueOf(String.valueOf(req.get("userId")));
        return ResponseDTO.ok(orderService.createPendingOrder(userId));
    }

    @PostMapping("/pay")
    public ResponseDTO<Void> pay(@RequestBody Map<String, Object> req) {
        Long orderId = Long.valueOf(String.valueOf(req.get("orderId")));
        Long userId = Long.valueOf(String.valueOf(req.get("userId")));
        orderService.markPaidAndFulfillCart(orderId, userId);
        return ResponseDTO.ok(null);
    }

    @GetMapping("/my/{userId}")
    public ResponseDTO<List<Order>> my(@PathVariable Long userId) {
        return ResponseDTO.ok(orderService.listMyOrders(userId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseDTO<Order> updateStatus(@PathVariable Long orderId, @RequestBody Map<String, String> req) {
        return ResponseDTO.ok(orderService.updateStatus(orderId, req.get("status")));
    }
}


