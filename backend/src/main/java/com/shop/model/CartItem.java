package com.shop.model;

import jakarta.persistence.*;

/**
 * 购物车项实体类
 */
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long cartId; // 所属购物车 ID

    @Column(nullable = false)
    private Long productId; // 商品 ID

    @Column(nullable = false)
    private Integer quantity; // 数量

    @Transient
    private Product product; // 商品信息（非数据库字段）

    // getter & setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
