package com.shop.config;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository) {
        return args -> {
            String adminUsername = "root";
            if (userRepository.existsByUsername(adminUsername)) {
                return;
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail("root@example.com");
            admin.setPassword(encoder.encode("041018"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        };
    }
}


