package com.example.demo.http;

import com.example.demo.model.types.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * 外部 API 客户端 — 使用 SP3 新的 RestClient
 *
 * ==========================================
 * 【SP2 → SP3 HTTP 客户端演进】
 * ==========================================
 *
 * 【SP2 时代】
 * - RestTemplate：同步阻塞，代码冗长
 * - WebClient：响应式，需要 Spring WebFlux
 *
 * 【SP3 推荐】
 * - RestClient（新增）：同步阻塞 + 流式 API，最优雅的选择
 * - 适合不需要响应式的场景（绝大多数项目）
 *
 * RestClient = RestTemplate 的易用性 + WebClient 的 API 风格
 */
@Component
@Slf4j
public class ExternalApiClient {

    private final RestClient restClient;

    public ExternalApiClient(RestClient.Builder builder) {
        // SP3 自动配置 RestClient.Builder
        // 可以在这里设置 baseUrl、默认 headers 等
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    /**
     * GET 请求示例 — 获取单个资源
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPost(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Map.class);
    }

    /**
     * GET 请求示例 — 获取列表
     */
    @SuppressWarnings("unchecked")
    public java.util.List<Map<String, Object>> getPosts() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(java.util.List.class);
    }

    /**
     * POST 请求示例 — 创建资源
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createPost(String title, String body) {
        Map<String, Object> requestBody = Map.of(
                "title", title,
                "body", body,
                "userId", 1
        );

        return restClient.post()
                .uri("/posts")
                .body(requestBody)
                .retrieve()
                .body(Map.class);
    }

    /**
     * 带错误处理的请求
     */
    public ApiResponse<Map<String, Object>> getPostSafe(Long id) {
        try {
            Map<String, Object> result = restClient.get()
                    .uri("/posts/{id}", id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                        log.warn("客户端错误: {}", response.getStatusCode());
                    })
                    .body(Map.class);
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("请求失败: {}", e.getMessage());
            return ApiResponse.error("外部API调用失败: " + e.getMessage());
        }
    }
}
