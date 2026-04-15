package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 用户更新请求 DTO — Record
 *
 * 更新请求的字段都是可选的（允许部分更新）
 * 使用 null 表示"不更新该字段"
 */
public record UserUpdateRequest(

        @Email(message = "邮箱格式不正确")
        String email,

        @Size(min = 6, max = 100, message = "密码长度6-100字符")
        String password,

        String displayName,

        String phone
) {}
