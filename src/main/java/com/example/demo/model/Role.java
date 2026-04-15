package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色实体
 *
 * 【设计说明】
 * - 角色和用户是多对多关系
 * - 使用 EAGER 加载角色（角色数据量小，始终需要）
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
