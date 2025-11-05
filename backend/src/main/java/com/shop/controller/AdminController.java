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

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    public AdminController(ProductService productService, OrderService orderService, UserRepository userRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // products
    @GetMapping("/products")
    public ResponseDTO<List<Product>> listProducts() {
        return ResponseDTO.ok(productService.listAll());
    }

    @PostMapping("/products")
    public ResponseDTO<Product> createProduct(@RequestBody Product p) {
        return ResponseDTO.ok(productService.create(p));
    }

    @PutMapping("/products/{id}")
    public ResponseDTO<Product> updateProduct(@PathVariable Long id, @RequestBody Product p) {
        return ResponseDTO.ok(productService.update(id, p));
    }

    @DeleteMapping("/products/{id}")
    public ResponseDTO<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseDTO.ok(null);
    }

    // orders (admin view)
    @GetMapping("/orders")
    public ResponseDTO<List<Order>> listOrders() {
        return ResponseDTO.ok(orderService.listAllOrders());
    }

    @PostMapping("/orders/{orderId}/ship")
    public ResponseDTO<Void> shipOrder(@PathVariable Long orderId) {
        orderService.updateStatus(orderId, "SHIPPING");
        return ResponseDTO.ok(null);
    }

    // users (admin view)
    @GetMapping("/users")
    public ResponseDTO<List<User>> listUsers() {
        return ResponseDTO.ok(userRepository.findAll());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseDTO<Void> deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(userId);
        return ResponseDTO.ok(null);
    }
}
