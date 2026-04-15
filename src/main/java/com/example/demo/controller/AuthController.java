package com.example.demo.controller;

import com.example.demo.model.types.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 认证控制器 — 登录/注册端点
 *
 * 简化版，实际项目应集成 JWT 或 Session 认证
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 登录端点
     * 实际项目：验证密码 → 生成 JWT Token → 返回
     * 这里做简化演示
     */
    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest request) {
        // 演示：实际应验证用户名密码
        if ("admin".equals(request.username()) && "admin123".equals(request.password())) {
            return ApiResponse.success("登录成功", Map.of(
                    "token", "demo-jwt-token-" + System.currentTimeMillis(),
                    "username", request.username()
            ));
        }
        return ApiResponse.error(401, "用户名或密码错误");
    }

    /**
     * 注册端点 — 委托给 UserController 的 createUser
     * 实际项目可在这里添加验证码、邀请码等逻辑
     */

    /**
     * 登录请求 — Record DTO
     */
    public record LoginRequest(
            @NotBlank(message = "用户名不能为空") String username,
            @NotBlank(message = "密码不能为空") String password
    ) {}
}
