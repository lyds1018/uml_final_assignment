package com.shop.controller;

import com.shop.dto.ResponseDTO;
import com.shop.model.User;
import com.shop.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseDTO<User> register(@RequestBody Map<String, String> req) {
        User u = userService.register(req.get("username"), req.get("password"));
        return ResponseDTO.ok(u);
    }

    @PostMapping("/login")
    public ResponseDTO<User> login(@RequestBody Map<String, String> req) {
        return userService.login(req.get("username"), req.get("password"))
                .map(ResponseDTO::ok)
                .orElseGet(() -> ResponseDTO.fail("用户名或密码错误"));
    }
}


