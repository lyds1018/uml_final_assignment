package com.shop.controller;

import com.shop.dto.ResponseDTO;
import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.service.UserService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    private Long resolveCurrentUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) throw new IllegalArgumentException("未登录");
        User u = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return u.getId();
    }

    @GetMapping
    public ResponseDTO<List<CartItem>> list(HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(cartService.getActiveCartItems(userId));
    }

    @PostMapping("/add")
    public ResponseDTO<CartItem> add(HttpServletRequest request, @RequestBody Map<String, Object> req) {
        Long userId = resolveCurrentUserId(request);
        Long productId = Long.valueOf(String.valueOf(req.get("productId")));
        int qty = Integer.parseInt(String.valueOf(req.get("quantity")));
        return ResponseDTO.ok(cartService.addItem(userId, productId, qty));
    }

    @PutMapping("/items/{itemId}")
    public ResponseDTO<Void> update(@PathVariable Long itemId, @RequestBody Map<String, Object> req) {
        int qty = Integer.parseInt(String.valueOf(req.get("quantity")));
        cartService.updateItemQuantity(itemId, qty);
        return ResponseDTO.ok(null);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseDTO<Void> remove(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseDTO.ok(null);
    }
}


