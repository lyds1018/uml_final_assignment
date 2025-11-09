package com.shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.ResponseDTO;
import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.OrderService;
import com.shop.service.ProductService;

@RestController                                    // 后台管理接口控制器
@RequestMapping("/api/admin")                       // 管理端统一前缀
public class AdminController {
    private final ProductService productService;   // 商品管理服务
    private final OrderService orderService;       // 订单管理服务
    private final UserRepository userRepository;   // 用户直接查询/管理

    public AdminController(
            ProductService productService,
            OrderService orderService,
            UserRepository userRepository
    ) {
        this.productService = productService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // ===================== 商品管理 =====================

    // 查询所有商品（管理员视角）
    @GetMapping("/products")
    public ResponseDTO<List<Product>> listProducts() {
        return ResponseDTO.ok(productService.listAll());
    }

    // 新增商品
    @PostMapping("/products")
    public ResponseDTO<Product> createProduct(@RequestBody Product p) {
        return ResponseDTO.ok(productService.create(p));
    }

    // 更新商品
    @PutMapping("/products/{id}")
    public ResponseDTO<Product> updateProduct(@PathVariable Long id, @RequestBody Product p) {
        return ResponseDTO.ok(productService.update(id, p));
    }

    // 删除商品
    @DeleteMapping("/products/{id}")
    public ResponseDTO<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseDTO.ok(null);
    }

    // ===================== 订单管理 =====================

    // 查询所有订单（管理员视角）
    @GetMapping("/orders")
    public ResponseDTO<List<Order>> listOrders() {
        return ResponseDTO.ok(orderService.listAllOrders());
    }

    // 管理员发货（将订单状态更新为 SHIPPING）
    @PostMapping("/orders/{orderId}/ship")
    public ResponseDTO<Void> shipOrder(@PathVariable Long orderId) {
        orderService.updateStatus(orderId, "SHIPPING");
        return ResponseDTO.ok(null);
    }

    // ===================== 用户管理 =====================

    // 查询所有用户
    @GetMapping("/users")
    public ResponseDTO<List<User>> listUsers() {
        return ResponseDTO.ok(userRepository.findAll());
    }

    // 删除用户
    @DeleteMapping("/users/{userId}")
    public ResponseDTO<Void> deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(userId);
        return ResponseDTO.ok(null);
    }
}

