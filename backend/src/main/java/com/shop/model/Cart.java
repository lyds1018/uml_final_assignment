package com.shop.model;

import jakarta.persistence.*;

@Entity                                             // 购物车实体类，对应一个用户的购物车记录
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                // 购物车 ID（自增主键）

    @Column(nullable = false)
    private Long userId;                            // 购物车所属用户 ID

    // ===== Getter / Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}



