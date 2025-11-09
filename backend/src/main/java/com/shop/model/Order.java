package com.shop.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

@Entity                                             // 订单实体类
@Table(name = "orders")                             // 对应数据库表 orders
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                // 订单主键 ID

    @Column(nullable = false)
    private Long userId;                            // 下单用户 ID

    @Column(nullable = false)
    private Double totalPrice;                      // 订单总金额

    @Column(nullable = false)
    private String status;                          // 订单状态：Pending / Paid / Shipping / Completed / Cancelled

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now(); // 创建时间（默认当前时间）

    @Transient
    private List<OrderItem> items;                  // 订单项列表（非数据库字段，通过 Service 加载）

    @Transient
    private User user;                              // 下单用户信息（非数据库字段，用于管理员后台展示）

    // ===== Getter / Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
