package com.example.demo.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 自定义自动配置 — 审计功能
 *
 * ==========================================
 * 【SP2 → SP3 自动装配注册方式变更】
 * ==========================================
 *
 * 【SP2 方式】
 * 在 META-INF/spring.factories 中注册：
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
 *   com.example.demo.autoconfig.AuditAutoConfiguration
 *
 * 【SP3 方式】
 * 在 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 中
 * 直接写全限定类名（每行一个）
 *
 * 为什么改？
 * - spring.factories 是全局文件，所有自动配置挤在一起
 * - imports 文件是专用于自动配置的，更清晰
 * - SP3 启动速度更快（按需加载 imports 而非扫描整个 spring.factories）
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "app",
        name = "audit-enabled",
        havingValue = "true",
        matchIfMissing = true  // 默认启用
)
@Slf4j
public class AuditAutoConfiguration {

    /**
     * 审计服务 Bean — 条件装配
     * 只有当 app.audit-enabled=true 时才创建
     */
    @Bean
    public AuditService auditService() {
        log.info(">>> 审计服务自动配置已启用");
        return new AuditService();
    }

    /**
     * 审计服务 — 简化示例
     */
    @Slf4j
    public static class AuditService {

        public void logAction(String action, String detail) {
            log.info("[AUDIT] {} - {}", action, detail);
        }
    }
}
