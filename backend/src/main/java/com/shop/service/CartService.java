package com.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.model.Cart;
import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ProductRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart c = new Cart();
            c.setUserId(userId);
            return cartRepository.save(c);
        });
    }

    public List<CartItem> getActiveCartItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        for (CartItem item : items) {
            item.setProduct(productRepository.findById(item.getProductId()).orElse(null));
        }
        return items;
    }

    public CartItem addItem(Long userId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("数量必须>0");
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("商品不存在"));
        if (product.getStock() < quantity) throw new IllegalArgumentException("库存不足");
        Cart cart = getOrCreateCart(userId);
        CartItem item = new CartItem();
        item.setCartId(cart.getId());
        item.setProductId(productId);
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void updateItemQuantity(Long itemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("数量必须>0");
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("购物车项不存在"));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }
}


