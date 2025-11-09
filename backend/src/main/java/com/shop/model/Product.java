package com.shop.model;

import jakarta.persistence.*;

@Entity                                             // 产品实体类，对应数据库中的 product 表
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                // 商品 ID，自增主键

    @Column(nullable = false)
    private String name;                            // 商品名称

    @Column(nullable = false)
    private Double price;                           // 商品价格

    @Column(nullable = false)
    private Integer stock;                          // 库存数量

    private String category;                        // 分类（可选）

    @Lob
    private String description;                     // 商品描述（使用大字段保存长文本）

    // ===== Getter / Setter 方法 =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}



