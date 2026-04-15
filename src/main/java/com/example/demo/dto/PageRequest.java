package com.example.demo.dto;

/**
 * 分页请求参数 — Record
 *
 * 【为什么不用 Spring Data 的 Pageable？】
 * - Spring Data 的 Pageable 是接口，不能用 Record
 * - 自定义 Record 包装可以添加默认值和校验
 * - 更好地控制 API 参数命名
 *
 * @param page 页码（从0开始）
 * @param size 每页大小
 * @param sortBy 排序字段
 * @param sortDir 排序方向（asc/desc）
 */
public record PageRequest(
        int page,
        int size,
        String sortBy,
        String sortDir
) {
    /**
     * 紧凑构造器 — 参数校验和默认值
     */
    public PageRequest {
        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        if (sortDir == null || sortDir.isBlank()) sortDir = "desc";
    }

    /**
     * 无参默认值 — 使用全参构造器传默认值
     */
    public PageRequest() {
        this(0, 10, "createdAt", "desc");
    }

    /**
     * 转换为 Spring Data 的 Pageable
     */
    public org.springframework.data.domain.PageRequest toPageable() {
        var direction = "asc".equalsIgnoreCase(sortDir)
                ? org.springframework.data.domain.Sort.Direction.ASC
                : org.springframework.data.domain.Sort.Direction.DESC;
        return org.springframework.data.domain.PageRequest.of(page, size, direction, sortBy);
    }
}
