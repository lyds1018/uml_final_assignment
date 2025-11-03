package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder(10);
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
        u.setPassword(encoder.encode(rawPassword));
        return userRepository.save(u);
    }

    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> encoder.matches(rawPassword, u.getPassword()));
    }
}


