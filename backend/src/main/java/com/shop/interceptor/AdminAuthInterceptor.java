package com.shop.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.shop.model.User;
import com.shop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 管理员权限拦截器
 * 
 * 作用：
 * 1. 拦截所有标记为需要管理员权限的请求（例如 /api/admin/**）。
 * 2. 检查请求中 username 属性对应的用户是否存在且角色为 ADMIN。
 * 3. 如果未登录或不是管理员，返回对应 HTTP 状态码并阻止访问。
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public AdminAuthInterceptor(UserService userService) {
        this.userService = userService;
    }

    /**
     * 请求处理前执行
     * 
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器对象
     * @return true 允许请求继续执行，false 拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求中获取 username（通常在认证拦截器中设置）
        String username = (String) request.getAttribute("username");
        if (username == null) {
            response.setStatus(401); // 未登录
            return false;           // 拦截请求
        }

        // 查询用户信息
        User user = userService.findByUsername(username).orElse(null);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.setStatus(403); // 权限不足
            return false;           // 拦截请求
        }

        return true; // 用户存在且为管理员，允许请求继续
    }
}
