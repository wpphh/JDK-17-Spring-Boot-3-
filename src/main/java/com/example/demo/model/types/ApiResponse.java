package com.example.demo.model.types;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * JDK 17 特性：Record 类
 *
 * 【JDK8 对比】
 * - JDK8 需要写一堆 getter/setter/toString/equals/hashCode 或用 Lombok @Data
 * - JDK17 的 Record 自动提供：构造器、getter、toString、equals、hashCode
 * - Record 是不可变的（immutable），天然线程安全
 *
 * 【使用场景】
 * - DTO（数据传输对象）
 * - 值对象
 * - API 统一响应包装
 *
 * @param <T> 数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int code,           // 状态码
        String message,     // 消息
        T data              // 数据
) {

    /**
     * 静态工厂方法 — 成功响应
     * Record 的静态方法和普通类一样
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * 静态工厂方法 — 失败响应
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }

    /**
     * Record 也可以有自定义的 compact 构造器做参数校验
     */
    public ApiResponse {
        if (code < 0) {
            throw new IllegalArgumentException("code 不能为负数");
        }
    }
}
