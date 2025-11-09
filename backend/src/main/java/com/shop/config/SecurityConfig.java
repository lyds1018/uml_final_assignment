package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    /**
     * 配置全局 CORS 策略
     * 允许前端从不同域（如 http://localhost:3000）访问后端接口
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建 URL 映射配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);         // 允许发送 Cookie
        config.addAllowedOrigin("http://localhost:3000"); // 允许访问的前端域名
        config.addAllowedHeader("*");             // 允许所有请求头
        config.addAllowedMethod("*");             // 允许所有请求方法（GET, POST, PUT, DELETE 等）

        // 将配置应用到所有 URL 路径
        source.registerCorsConfiguration("/**", config);

        // 返回 CORS 过滤器
        return new CorsFilter(source);
    }
}
