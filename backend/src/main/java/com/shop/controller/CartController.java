package com.shop.controller;

import com.shop.dto.ResponseDTO;
import com.shop.model.CartItem;
import com.shop.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseDTO<List<CartItem>> list(@PathVariable Long userId) {
        return ResponseDTO.ok(cartService.getActiveCartItems(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseDTO<CartItem> add(@PathVariable Long userId, @RequestBody Map<String, Object> req) {
        Long productId = Long.valueOf(String.valueOf(req.get("productId")));
        int qty = Integer.parseInt(String.valueOf(req.get("quantity")));
        return ResponseDTO.ok(cartService.addItem(userId, productId, qty));
    }

    @PutMapping("/item/{itemId}")
    public ResponseDTO<Void> update(@PathVariable Long itemId, @RequestBody Map<String, Object> req) {
        int qty = Integer.parseInt(String.valueOf(req.get("quantity")));
        cartService.updateItemQuantity(itemId, qty);
        return ResponseDTO.ok(null);
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseDTO<Void> remove(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseDTO.ok(null);
    }
}


