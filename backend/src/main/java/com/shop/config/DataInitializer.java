package com.shop.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository) {
        return args -> {
            String adminUsername = "root";
            if (userRepository.existsByUsername(adminUsername)) {
                return;
            }
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail("root@example.com");
            String hashedPassword = BCrypt.withDefaults().hashToString(12, "041018".toCharArray());
            admin.setPassword(hashedPassword);
            admin.setRole("ADMIN");
            userRepository.save(admin);
        };
    }
}


