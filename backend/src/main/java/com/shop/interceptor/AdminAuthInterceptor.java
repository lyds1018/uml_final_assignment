package com.shop.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.shop.model.User;
import com.shop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public AdminAuthInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            response.setStatus(401);
            return false;
        }

        User user = userService.findByUsername(username).orElse(null);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.setStatus(403);
            return false;
        }

        return true;
    }
}