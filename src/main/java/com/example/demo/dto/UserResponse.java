package com.example.demo.dto;

import com.example.demo.model.User;
import com.example.demo.model.types.AccountStatus;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户响应 DTO — Record
 *
 * 从 User 实体转换而来，不暴露敏感字段（如 password）
 *
 * @param id 用户ID
 * @param username 用户名
 * @param email 邮箱
 * @param displayName 显示名
 * @param phone 电话
 * @param status 账户状态
 * @param roles 角色集合
 * @param createdAt 创建时间
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        String displayName,
        String phone,
        AccountStatus status,
        Set<String> roles,
        LocalDateTime createdAt
) {
    /**
     * 静态工厂方法 — 从实体转换
     * 比在 Service 层写转换逻辑更清晰
     */
    public static UserResponse from(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getPhone(),
                user.getStatus(),
                roleNames,
                user.getCreatedAt()
        );
    }
}
