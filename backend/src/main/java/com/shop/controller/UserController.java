package com.shop.controller;

import com.shop.dto.ResponseDTO;
import com.shop.model.User;
import com.shop.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController                                     // 声明为 REST 控制器，返回 JSON
@RequestMapping("/api/users")                       // 所有接口路径前缀为 /api/users
public class UserController {
    private final UserService userService;          // 用户业务逻辑层

    @Value("${app.security.jwt.secret}")            // 从配置文件中读取 JWT 密钥
    private String jwtSecret;

    @Value("${app.security.jwt.expiration:86400000}") // JWT 过期时间，默认 1 天
    private long jwtExpiration;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 用户注册接口
    @PostMapping("/register")
    public ResponseDTO<User> register(@RequestBody Map<String, String> req) {
        // 从请求 JSON 中取出用户名和密码，交给 service 处理
        User u = userService.register(req.get("username"), req.get("password"));
        return ResponseDTO.ok(u);                   // 返回统一响应格式
    }

    // 用户登录接口
    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        // 验证用户名和密码，service 返回 Optional<User>
        return userService.login(req.get("username"), req.get("password"))
                .map(u -> {
                    // 准备生成 JWT 所需的密钥
                    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

                    // 构建 JWT token
                    String token = Jwts.builder()
                            .setSubject(u.getUsername())                  // token 的主体信息
                            .claim("role", u.getRole())                   // 自定义字段，存储用户角色
                            .setIssuedAt(new Date())                      // 签发时间
                            .setExpiration(new Date(
                                    System.currentTimeMillis() + jwtExpiration)) // 设置过期时间
                            .signWith(key)                                // 使用密钥签名
                            .compact();                                   // 生成最终的 JWT 字符串

                    // 构建返回给前端的数据
                    Map<String, Object> body = new HashMap<>();
                    body.put("token", token);      // 前端后续请求需要使用
                    body.put("role", u.getRole()); // 用户角色信息
                    body.put("user", u);           // 用户基本信息

                    return ResponseDTO.ok(body);   // 登录成功
                })
                // 如果 Optional 为空，说明认证失败
                .orElseGet(() -> ResponseDTO.fail("用户名或密码错误"));
    }
}

