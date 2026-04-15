package com.example.demo.service.impl;

import com.example.demo.dto.PageRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * 【SP3 最佳实践】
 * - 使用构造器注入（Lombok @RequiredArgsConstructor 自动生成）
 * - 不再使用 @Autowired 字段注入（SP3 推荐构造器注入）
 * - @Transactional 声明式事务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("用户名已存在: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("邮箱已被注册: " + request.email());
        }

        // 创建用户实体
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password()); // 实际项目应加密
        user.setDisplayName(request.displayName());

        User saved = userRepository.save(user);
        log.info("创建用户成功: {}", saved.getUsername());
        return UserResponse.from(saved);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));
        return UserResponse.from(user);
    }

    @Override
    public List<UserResponse> listUsers(PageRequest pageRequest) {
        // 使用 Record 的方法转换为 Spring Data Pageable
        var pageable = pageRequest.toPageable();
        Page<User> page = userRepository.findAll(pageable);
        return page.getContent().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));

        // 部分更新 — 只更新非空字段
        if (request.email() != null) {
            if (userRepository.existsByEmail(request.email()) && !user.getEmail().equals(request.email())) {
                throw new BusinessException("邮箱已被使用: " + request.email());
            }
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(request.password());
        }
        if (request.displayName() != null) {
            user.setDisplayName(request.displayName());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        }

        User updated = userRepository.save(user);
        log.info("更新用户成功: {}", updated.getUsername());
        return UserResponse.from(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("用户不存在，ID: " + id);
        }
        userRepository.deleteById(id);
        log.info("删除用户成功，ID: {}", id);
    }
}
