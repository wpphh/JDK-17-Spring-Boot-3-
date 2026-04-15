package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户创建请求 DTO — JDK 17 Record
 *
 * 【JDK8 对比】
 * - JDK8：需要写 class + 全部字段 + getter/setter + @Data(Lombok)
 * - JDK17：一行 Record 搞定，自动不可变、自带 getter/equals/hashCode/toString
 *
 * 【SP3 注意】
 * - jakarta.validation.constraints.* 而非 javax.validation.constraints.*
 * - Record 字段的 getter 方法名是 username() 而非 getUsername()
 *   但 Jackson 默认能正确序列化
 *
 * @param username 用户名
 * @param email 邮箱
 * @param password 密码
 * @param displayName 显示名（可选）
 */
public record UserCreateRequest(

        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50, message = "用户名长度3-50字符")
        String username,

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        String email,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 100, message = "密码长度6-100字符")
        String password,

        String displayName
) {}
