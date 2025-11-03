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

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration:86400000}")
    private long jwtExpiration;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseDTO<User> register(@RequestBody Map<String, String> req) {
        User u = userService.register(req.get("username"), req.get("password"));
        return ResponseDTO.ok(u);
    }

    @PostMapping("/login")
    public ResponseDTO<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        return userService.login(req.get("username"), req.get("password"))
                .map(u -> {
                    // generate JWT
                    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
                    String token = Jwts.builder()
                            .setSubject(u.getUsername())
                            .claim("role", u.getRole())
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                            .signWith(key)
                            .compact();

                    Map<String, Object> body = new HashMap<>();
                    body.put("token", token);
                    body.put("role", u.getRole());
                    body.put("user", u);
                    return ResponseDTO.ok(body);
                })
                .orElseGet(() -> ResponseDTO.fail("用户名或密码错误"));
    }
}


