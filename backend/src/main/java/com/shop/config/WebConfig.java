package com.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shop.interceptor.AdminAuthInterceptor;
import com.shop.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;           // 普通用户认证拦截器
    private final AdminAuthInterceptor adminAuthInterceptor; // 管理员认证拦截器

    public WebConfig(AuthInterceptor authInterceptor, AdminAuthInterceptor adminAuthInterceptor) {
        this.authInterceptor = authInterceptor;
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 普通用户认证拦截器
        // 拦截所有 /api/** 的请求，确保用户已登录
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**");
        
        // 2. 管理员认证拦截器
        // 拦截所有 /api/admin/** 的请求，确保用户是管理员
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/admin/**");
    }
}
