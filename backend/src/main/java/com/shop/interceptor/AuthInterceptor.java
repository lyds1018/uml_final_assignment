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

/**
 * 用户认证拦截器
 *
 * 功能：
 * 1. 验证请求中的 JWT 是否有效。
 * 2. 将用户名和角色信息写入 request 属性，供后续业务逻辑使用。
 * 3. 放行无需认证的接口（如登录注册、商品查询）。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final String jwtSecret;

    public AuthInterceptor(UserService userService, @Value("${app.security.jwt.secret}") String jwtSecret) {
        this.userService = userService;
        this.jwtSecret = jwtSecret;
    }

    /**
     * 请求处理前执行
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器对象
     * @return true 允许请求继续，false 拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        // -------------------------
        // 放行无需登录的接口
        // -------------------------
        // 登录、注册接口无需验证 token
        if (path.startsWith("/api/users/login") || path.startsWith("/api/users/register")) {
            return true;
        }
        // GET 请求的商品查询接口可公开访问
        if ("GET".equalsIgnoreCase(request.getMethod()) && path.startsWith("/api/products")) {
            return true;
        }

        // -------------------------
        // 获取并验证 JWT
        // -------------------------
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException("未登录"); // token 缺失或格式错误
        }

        try {
            // 使用配置的 secret 构建密钥
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            // 解析 JWT
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7)) // 去掉 "Bearer "
                    .getBody();

            String username = claims.getSubject();

            // 检查用户是否存在
            if (!userService.existsByUsername(username)) {
                throw new BusinessException("用户不存在");
            }

            // -------------------------
            // 管理员接口权限校验
            // -------------------------
            if (path.startsWith("/api/admin") && !"ADMIN".equals(claims.get("role"))) {
                throw new BusinessException("无权访问");
            }

            // 将用户名和角色写入 request，供控制器使用
            request.setAttribute("username", username);
            request.setAttribute("role", claims.get("role"));
            return true;

        } catch (Exception e) {
            // JWT 解析或验证失败
            throw new BusinessException("无效的token");
        }
    }
}
