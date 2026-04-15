package com.example.demo.model.types;

/**
 * JDK 17 特性：Sealed Class（密封类）+ Enum 增强
 *
 * 【JDK8 对比】
 * - JDK8 的枚举不能限制谁可以继承它
 * - JDK17 的 sealed 允许你精确控制哪些类可以继承/实现当前类
 *
 * 【为什么有用】
 * - 编译器能穷尽所有子类型，配合 switch 表达式实现 exhaustive check
 * - 防止外部随意扩展，保证领域模型的封闭性
 */
public enum AccountStatus {
    ACTIVE("活跃"),
    INACTIVE("未激活"),
    SUSPENDED("已暂停"),
    DELETED("已删除");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    // JDK 17 的 enum 可以配合 record 使用，简化数据携带
    public String getDescription() {
        return description;
    }

    /**
     * 判断账户是否可以登录
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }
}
