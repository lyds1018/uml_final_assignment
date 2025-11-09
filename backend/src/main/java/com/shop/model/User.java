package com.shop.model;

import jakarta.persistence.*;

@Entity                                             // 标识为 JPA 实体类
@Table(name = "users")                              // 映射到数据库表 users
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;                                // 主键，自增 ID

    @Column(nullable = false, unique = true)        
    private String username;                        // 用户名（全局唯一）

    @Column(nullable = false)
    private String password;                        // 密码

    @Column(nullable = false)
    private String role = "USER";                   // 用户角色，默认为普通用户（USER/ADMIN）

    // ===== Getter / Setter 方法 =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}


