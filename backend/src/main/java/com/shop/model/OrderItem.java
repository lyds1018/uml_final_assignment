package com.shop.model;

import jakarta.persistence.*;

@Entity                                            // 订单项实体类，对应订单中的每件商品
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                // 订单项 ID（自增主键）

    @Column(nullable = false)
    private Long orderId;                           // 所属订单 ID

    @Column(nullable = false)
    private Long productId;                         // 商品 ID，对应 Product

    @Column(nullable = false)
    private Integer quantity;                       // 购买数量

    @Column(nullable = false)
    private Double price;                           // 下单时的商品单价（快照）

    @Transient
    private Product product;                        // 商品详情（非数据库字段，由 Service 动态填充）

    // ===== Getter / Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
