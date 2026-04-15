package com.example.demo.controller;

import com.example.demo.dto.PageRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.model.types.ApiResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 REST 控制器
 *
 * 【SP2 → SP3 变化】
 * - jakarta.validation.Valid 而非 javax.validation.Valid
 * - 其余注解（@RestController, @RequestMapping 等）不变
 * - 返回 Record 类型 ApiResponse 自动序列化
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * POST /api/users — 创建用户
     */
    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return ApiResponse.success("创建成功", user);
    }

    /**
     * GET /api/users/{id} — 查询单个用户
     */
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    /**
     * GET /api/users — 分页查询用户列表
     */
    @GetMapping
    public ApiResponse<List<UserResponse>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        // 使用 Record 构造器创建分页参数
        PageRequest pageRequest = new PageRequest(page, size, sortBy, sortDir);
        List<UserResponse> users = userService.listUsers(pageRequest);
        return ApiResponse.success(users);
    }

    /**
     * PUT /api/users/{id} — 更新用户
     */
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        UserResponse user = userService.updateUser(id, request);
        return ApiResponse.success("更新成功", user);
    }

    /**
     * DELETE /api/users/{id} — 删除用户
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("删除成功", null);
    }
}
