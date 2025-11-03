package com.shop.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String rawPassword) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("用户名与密码不能为空");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User u = new User();
        u.setUsername(username);
        String hashedPassword = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
        u.setPassword(hashedPassword);
        return userRepository.save(u);
    }

    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> BCrypt.verifyer()
                        .verify(rawPassword.getBytes(StandardCharsets.UTF_8), 
                               u.getPassword().getBytes(StandardCharsets.UTF_8))
                        .verified);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

        public boolean existsByUsername(String username) {
            return userRepository.existsByUsername(username);
        }
}


