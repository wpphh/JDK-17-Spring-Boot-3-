package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置属性 — 使用 @ConfigurationProperties 绑定
 *
 * 【SP2 → SP3 变化】
 * - @ConfigurationProperties 的包从 org.springframework.boot.context.properties 不变
 * - SP3 推荐使用 @ConfigurationProperties 而非 @Value
 * - 支持松散绑定（app.name = app.Name = app.name 都可以）
 *
 * 对应 application.yml 中的 app.* 配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /**
     * 应用名称
     */
    private String name = "JDK17 SP3 Scaffold";

    /**
     * 应用版本
     */
    private String version = "1.0.0";

    /**
     * 是否启用审计日志
     */
    private boolean auditEnabled = true;

    /**
     * 分页默认大小
     */
    private int defaultPageSize = 10;

    /**
     * JWT 密钥（生产环境应从环境变量读取）
     */
    private String jwtSecret = "change-me-in-production";
}
