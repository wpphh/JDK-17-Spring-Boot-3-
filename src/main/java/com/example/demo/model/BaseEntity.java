package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA 实体基类 — 通用审计字段
 *
 * 【SP2 → SP3 变化】
 * - 包名从 javax.persistence.* 变为 jakarta.persistence.*
 * - 这是 SP3 最大的破坏性变更，所有 javax 都要换成 jakarta
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 主键 — 使用 Long + 自增策略
     * IDENTITY 策略依赖数据库自增列
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间 — Hibernate 自动填充
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间 — Hibernate 自动维护
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 乐观锁版本号 — 防止并发更新冲突
     */
    @Version
    private Long version;
}
