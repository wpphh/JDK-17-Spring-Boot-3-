package com.example.demo.service;

import com.example.demo.dto.PageRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserUpdateRequest;

import java.util.List;

/**
 * 用户服务接口
 *
 * 定义业务操作，不暴露实现细节
 */
public interface UserService {

    /**
     * 创建用户
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * 根据 ID 查询用户
     */
    UserResponse getUserById(Long id);

    /**
     * 分页查询用户列表
     */
    List<UserResponse> listUsers(PageRequest pageRequest);

    /**
     * 更新用户
     */
    UserResponse updateUser(Long id, UserUpdateRequest request);

    /**
     * 删除用户
     */
    void deleteUser(Long id);
}
