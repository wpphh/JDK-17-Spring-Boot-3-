package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户 Repository — Spring Data JPA
 *
 * 【SP2 → SP3 变化】
 * - Spring Data 的 API 基本没变，但底层使用 Jakarta Persistence
 * - 方法名查询（Derived Query Methods）依然支持
 * - @Query 中的 JPQL 使用实体属性名，不是表名
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询 — 返回 Optional（推荐做法，避免 NPE）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询
     */
    Optional<User> findByEmail(String email);

    /**
     * 判断用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 判断邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 自定义 JPQL 查询 — 使用 @Query 注解
     * :status 是命名参数，对应 @Param("status")
     */
    @Query("SELECT u FROM User u WHERE u.status = :status ORDER BY u.createdAt DESC")
    List<User> findByStatus(@Param("status") com.example.demo.model.types.AccountStatus status);

    /**
     * 模糊搜索 — 按用户名或显示名搜索
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.displayName LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
