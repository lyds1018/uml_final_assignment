package com.shop.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    /**
     * 初始化管理员用户
     * CommandLineRunner 会在 Spring Boot 启动后执行 run 方法
     */
    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository) {
        return args -> {
            String adminUsername = "root"; // 管理员用户名

            // 如果数据库中已经存在管理员账号，则不再创建
            if (userRepository.existsByUsername(adminUsername)) {
                return;
            }

            // 创建管理员用户
            User admin = new User();
            admin.setUsername(adminUsername);

            // 使用 BCrypt 哈希算法加密密码
            String hashedPassword = BCrypt.withDefaults().hashToString(
                    12,                  // 加密强度，值越大越安全但耗时越长
                    "041018".toCharArray() // 明文密码
            );
            admin.setPassword(hashedPassword);

            admin.setRole("ADMIN"); // 设置角色为管理员

            // 保存到数据库
            userRepository.save(admin);
        };
    }
}



