package com.example.demo.model;

import com.example.demo.model.types.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体
 *
 * 【SP2 → SP3 关键变化】
 * 1. jakarta.persistence.* 而非 javax.persistence.*
 * 2. jakarta.validation.constraints.* 而非 javax.validation.*
 * 3. EnumType.STRING 推荐用字符串存储枚举（而非序号），更安全
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(length = 20)
    private String phone;

    /**
     * 账户状态 — 使用枚举类型
     * EnumType.STRING 存储枚举名（如 "ACTIVE"），比 ORDINAL 更安全
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    /**
     * 多对多关系 — 用户与角色
     * JoinTable 定义中间表
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * 便捷方法 — 添加角色
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }
}
