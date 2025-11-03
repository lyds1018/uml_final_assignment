package com.shop.interceptor;

import com.shop.exception.BusinessException;
import com.shop.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final String jwtSecret;

    public AuthInterceptor(UserService userService, @Value("${app.security.jwt.secret}") String jwtSecret) {
        this.userService = userService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        // 放行登录注册接口
        if (path.startsWith("/api/users/login") || path.startsWith("/api/users/register")) {
            return true;
        }
        // 放行商品查询接口（GET 可公开访问）
        if ("GET".equalsIgnoreCase(request.getMethod()) && path.startsWith("/api/products")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException("未登录");
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7))
                    .getBody();

            String username = claims.getSubject();
            if (!userService.existsByUsername(username)) {
                throw new BusinessException("用户不存在");
            }

            // 对管理员接口进行权限验证
            if (path.startsWith("/api/admin") && !"ADMIN".equals(claims.get("role"))) {
                throw new BusinessException("无权访问");
            }

            request.setAttribute("username", username);
            request.setAttribute("role", claims.get("role"));
            return true;
        } catch (Exception e) {
            throw new BusinessException("无效的token");
        }
    }
}