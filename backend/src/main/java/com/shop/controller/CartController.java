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

@RestController                                    // 标记为 REST 控制器，返回 JSON
@RequestMapping("/api/cart")                       // 所有接口前缀为 /api/cart
public class CartController {
    private final CartService cartService;         // 购物车业务逻辑层
    private final UserService userService;         // 用户业务逻辑层，用于解析用户身份

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // 从 request 中解析当前登录用户 ID（由 JWT 过滤器提前设置 username）
    private Long resolveCurrentUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) throw new IllegalArgumentException("未登录");

        User u = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return u.getId();
    }

    // 查询购物车列表（当前用户的所有有效购物车项）
    @GetMapping
    public ResponseDTO<List<CartItem>> list(HttpServletRequest request) {
        Long userId = resolveCurrentUserId(request);
        return ResponseDTO.ok(cartService.getActiveCartItems(userId));
    }

    // 添加商品到购物车
    @PostMapping("/add")
    public ResponseDTO<CartItem> add(HttpServletRequest request, @RequestBody Map<String, Object> req) {
        Long userId = resolveCurrentUserId(request);

        // 从请求体中取商品 ID 和数量
        Long productId = Long.valueOf(String.valueOf(req.get("productId")));
        int qty = Integer.parseInt(String.valueOf(req.get("quantity")));

        return ResponseDTO.ok(cartService.addItem(userId, productId, qty));
    }

    // 更新购物车某项的数量（增减数量都通过这个接口）
    @PutMapping("/items/{itemId}")
    public ResponseDTO<Void> update(@PathVariable Long itemId, @RequestBody Map<String, Object> req) {
        int qty = Integer.parseInt(String.valueOf(req.get("quantity"))); // 新数量
        cartService.updateItemQuantity(itemId, qty);
        return ResponseDTO.ok(null);
    }

    // 删除购物车中的某项
    @DeleteMapping("/items/{itemId}")
    public ResponseDTO<Void> remove(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseDTO.ok(null);
    }
}



