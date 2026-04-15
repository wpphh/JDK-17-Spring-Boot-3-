# JDK 17 + Spring Boot 3 全栈脚手架项目

> 一份面向从 JDK 8 + Spring Boot 2 迁移到 JDK 17 + Spring Boot 3 开发者的完整学习指南。

---

## 1. 项目概述

本项目是一个 **JDK 17 + Spring Boot 3.4** 脚手架项目，旨在帮助开发者快速上手新一代 Java 技术栈。项目以用户管理（User CRUD）为业务场景，完整演示了以下核心能力：

- JDK 17 新特性在实际项目中的落地（Record、Sealed Class、Pattern Matching、Text Block、Switch 表达式）
- Spring Boot 3 破坏性变更的应对方案（jakarta 命名空间、SecurityFilterChain、RestClient、AutoConfiguration.imports）
- 标准分层架构：Controller → Service → Repository → Entity
- 统一 API 响应封装、全局异常处理、参数校验
- 多环境配置（dev / prod）

**适用人群**：正在或将要从 Spring Boot 2.x 迁移到 3.x 的 Java 后端开发者。

---

## 2. 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 17 | SP3 最低要求，启用 Record / Sealed / Pattern Matching |
| Spring Boot | 3.4.4 | 当前最新稳定版 |
| Spring Web MVC | 6.x（随 SP3） | RESTful Web 层 |
| Spring Data JPA | 3.x（随 SP3） | 数据持久化，使用 jakarta.persistence |
| Spring Security | 6.x（随 SP3） | 安全框架，SecurityFilterChain 模式 |
| Spring Validation | 3.x（随 SP3） | 参数校验，使用 jakarta.validation |
| Spring Actuator | 3.x（随 SP3） | 监控端点 |
| H2 Database | 随 SP3 BOM | 内存数据库，开发测试用 |
| Lombok | 随 SP3 BOM | 减少样板代码 |
| JUnit 5 | 随 SP3 BOM | 单元测试框架 |
| Maven | 3.6+ | 构建工具 |

---

## 3. 项目结构

```
javaweb/
├── pom.xml                                          # Maven 构建配置
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java                 # 启动类
│   │   │   ├── autoconfig/
│   │   │   │   └── AuditAutoConfiguration.java      # 自定义自动配置（演示 AutoConfiguration.imports）
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java                   # @ConfigurationProperties 配置绑定
│   │   │   │   └── SecurityConfig.java              # Spring Security 6 配置（SecurityFilterChain）
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java              # 认证端点（登录）
│   │   │   │   └── UserController.java              # 用户 CRUD 端点
│   │   │   ├── dto/
│   │   │   │   ├── PageRequest.java                 # 分页请求 Record DTO
│   │   │   │   ├── UserCreateRequest.java           # 创建用户请求 Record DTO
│   │   │   │   ├── UserResponse.java                # 用户响应 Record DTO
│   │   │   │   └── UserUpdateRequest.java           # 更新用户请求 Record DTO
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java           # 业务异常
│   │   │   │   ├── GlobalExceptionHandler.java      # 全局异常处理器
│   │   │   │   └── ResourceNotFoundException.java   # 资源未找到异常
│   │   │   ├── http/
│   │   │   │   └── ExternalApiClient.java           # RestClient 外部调用示例
│   │   │   ├── model/
│   │   │   │   ├── BaseEntity.java                  # JPA 实体基类
│   │   │   │   ├── Role.java                        # 角色实体
│   │   │   │   ├── User.java                        # 用户实体
│   │   │   │   └── types/
│   │   │   │       ├── AccountStatus.java           # 账户状态枚举
│   │   │   │       └── ApiResponse.java             # 统一 API 响应 Record
│   │   │   ├── repository/
│   │   │   │   ├── RoleRepository.java              # 角色数据访问
│   │   │   │   └── UserRepository.java              # 用户数据访问
│   │   │   ├── service/
│   │   │   │   ├── UserService.java                 # 用户服务接口
│   │   │   │   └── impl/
│   │   │   │       └── UserServiceImpl.java         # 用户服务实现
│   │   │   └── util/
│   │   │       └── PatternMatchingDemo.java          # JDK 17 特性合集演示
│   │   └── resources/
│   │       ├── application.yml                      # 通用配置
│   │       ├── application-dev.yml                  # 开发环境配置
│   │       ├── application-prod.yml                 # 生产环境配置
│   │       ├── schema.sql                           # 数据库表结构初始化
│   │       ├── data.sql                             # 初始数据
│   │       └── META-INF/spring/
│   │           └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│   └── test/
│       └── java/com/example/demo/util/
│           └── PatternMatchingDemoTest.java          # JDK 17 特性单元测试
```

---

## 4. JDK 17 新特性详解

### 4.1 Record 类（不可变数据载体）

**代码位置**：`src/main/java/com/example/demo/dto/` 下所有文件、`ApiResponse.java`

Record 是 JDK 16 正式引入的语法糖，自动生成 `final` 字段、构造器、`getter`、`equals`、`hashCode`、`toString`。它天然不可变，非常适合做 DTO 和值对象。

#### JDK 8 写法

```java
public class UserCreateRequest {
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
    private String displayName;

    // 需要 getter / setter / equals / hashCode / toString
    // 或者依赖 Lombok @Data
}
```

#### JDK 17 写法

```java
public record UserCreateRequest(
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 6, max = 100)
        String password,

        String displayName
) {}
```

**关键差异**：

| 对比项 | JDK 8 Class | JDK 17 Record |
|--------|-------------|---------------|
| 字段 | mutable（可变） | `final`（不可变） |
| getter | `getUsername()` | `username()`（无 get 前缀） |
| setter | 有 | **无**（不可变） |
| equals/hashCode | 需手写或 Lombok | 自动生成 |
| toString | 需手写或 Lombok | 自动生成 |
| 适用场景 | 通用 | DTO、值对象、API 响应 |

**本项目中的 Record 使用**：

- `UserCreateRequest` — 创建用户请求 DTO，配合 `@Valid` 校验
- `UserUpdateRequest` — 更新用户请求 DTO（字段可选，null 表示不更新）
- `UserResponse` — 用户响应 DTO，含静态工厂方法 `from(User user)`
- `PageRequest` — 分页参数，含紧凑构造器做默认值处理
- `ApiResponse<T>` — 统一 API 响应封装，含静态工厂方法
- `AuthController.LoginRequest` — 内部 Record，登录请求 DTO

#### Record 的紧凑构造器

`PageRequest` 展示了 Record 紧凑构造器的用法——在构造时自动修正非法参数：

```java
public record PageRequest(int page, int size, String sortBy, String sortDir) {
    public PageRequest {
        if (page < 0) page = 0;          // 自动修正负数页码
        if (size < 1) size = 10;         // 最小每页 10 条
        if (size > 100) size = 100;      // 最大每页 100 条
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        if (sortDir == null || sortDir.isBlank()) sortDir = "desc";
    }
}
```

---

### 4.2 Sealed Class（密封类）

**代码位置**：`PatternMatchingDemo.java`（`Shape` 接口及其子类）、`AccountStatus.java`

Sealed Class（JDK 17 正式引入）允许你精确控制哪些类可以继承或实现当前类/接口。编译器因此能穷尽所有子类型，配合 Switch 表达式可省略 `default` 分支。

#### JDK 8 做法

JDK 8 无法限制继承。你只能靠文档约定"不要扩展这个类"，编译器无法帮你检查。

#### JDK 17 写法

```java
// 密封接口 — 只允许 Circle、Rectangle、Triangle 实现
public sealed interface Shape permits Circle, Rectangle, Triangle {
    double area();
}

// 子类必须是 final、sealed 或 non-sealed
public record Circle(double radius) implements Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}

public record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() { return width * height; }
}

public record Triangle(double base, double height) implements Shape {
    @Override
    public double area() { return 0.5 * base * height; }
}
```

**Sealed + Switch 穷尽检查**：编译器知道所有子类型，不需要 `default`：

```java
public static String describeShape(Shape shape) {
    return switch (shape) {
        case Circle c   -> "圆形，半径=" + c.radius();
        case Rectangle r -> "矩形，宽=" + r.width();
        case Triangle t  -> "三角形，底=" + t.base();
        // 不需要 default！漏了子类型编译直接报错
    };
}
```

**本项目中的 Sealed 使用**：`AccountStatus` 枚举虽然不是 sealed class，但展示了与 sealed 配合的枚举设计思路。`PatternMatchingDemo.Shape` 是完整的 sealed interface 演示。

---

### 4.3 Pattern Matching for instanceof

**代码位置**：`PatternMatchingDemo.java` 的 `describeObject()` 方法

#### JDK 8 写法

```java
if (obj instanceof String) {
    String s = (String) obj;  // 必须手动强转
    return s.length();
}
```

#### JDK 17 写法

```java
if (obj instanceof String s) {  // 自动绑定变量 s，无需强转
    return s.length();
}
```

**JDK 16+ 增强**：可直接加条件判断：

```java
if (obj instanceof String s && s.length() > 5) {
    return "长字符串: " + s;  // s 在 && 右侧也可用
}
```

**本项目示例**：

```java
public static String describeObject(Object obj) {
    if (obj instanceof String s) {
        return "字符串，长度=" + s.length();
    } else if (obj instanceof Integer i) {
        return "整数，值=" + i;
    } else if (obj instanceof Long l) {
        return "长整数，值=" + l;
    } else if (obj instanceof Double d) {
        return "浮点数，值=" + d;
    } else if (obj == null) {
        return "null";
    }
    return "未知类型: " + obj.getClass().getSimpleName();
}
```

---

### 4.4 Text Block（文本块）

**代码位置**：`PatternMatchingDemo.java` 的 `buildHtmlTemplate()` 和 `buildSelectQuery()` 方法

Text Block（JDK 15 正式引入）使用三引号 `"""` 定义多行字符串，保留缩进和换行。

#### JDK 8 写法

```java
String sql = "SELECT u.id, u.name, u.email "
           + "FROM users u "
           + "WHERE u.status = 'ACTIVE' "
           + "ORDER BY u.created_at DESC";
```

#### JDK 17 写法

```java
String sql = """
        SELECT u.id, u.name, u.email
        FROM users u
        WHERE u.status = 'ACTIVE'
        ORDER BY u.created_at DESC
        """;
```

**配合 `.formatted()` 使用**：

```java
public static String buildHtmlTemplate() {
    return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>%s</title>
            </head>
            <body>
                <h1>欢迎</h1>
                <p>这是一个 JDK 17 Text Block 示例</p>
            </body>
            </html>
            """.formatted("Demo Page");
}

public static String buildSelectQuery(String tableName) {
    return """
            SELECT *
            FROM %s
            WHERE status = 'ACTIVE'
            ORDER BY created_at DESC
            LIMIT 10
            """.formatted(tableName);
}
```

**适用场景**：SQL 语句、JSON 模板、HTML 模板、正则表达式等多行文本。

---

### 4.5 Switch 表达式

**代码位置**：`PatternMatchingDemo.java` 的 `levelToString()` 和 `dayType()` 方法

Switch 表达式（JDK 14 Preview，JDK 17 正式版）用箭头语法 `->` 替代了传统的 case-break 结构。

#### JDK 8 写法

```java
String result;
switch (level) {
    case 1:
        result = "LOW";
        break;
    case 2:
        result = "MEDIUM";
        break;
    case 3:
        result = "HIGH";
        break;
    default:
        result = "UNKNOWN";
        break;
}
```

常见坑：忘记写 `break` 导致 fall-through（贯穿）。

#### JDK 17 写法

```java
String result = switch (level) {
    case 1 -> "LOW";
    case 2 -> "MEDIUM";
    case 3 -> "HIGH";
    default -> "UNKNOWN";
};
```

**关键优势**：
- 不需要 `break`（不会 fall-through）
- 是表达式，可直接赋值
- 多个 case 合并用逗号：`case "sat", "sun" -> "周末"`
- 多行分支用 `yield` 返回值：

```java
public static String dayType(String day) {
    return switch (day.toLowerCase()) {
        case "monday", "tuesday", "wednesday", "thursday", "friday" -> {
            String result = day + " 是工作日";
            yield result;  // yield 在代码块中返回值
        }
        case "saturday", "sunday" -> day + " 是周末";
        default -> throw new IllegalArgumentException("无效的星期: " + day);
    };
}
```

---

## 5. Spring Boot 3 核心变化详解

### 5.1 jakarta 命名空间（最大的破坏性变更）

**代码位置**：`BaseEntity.java`、`User.java`、`Role.java`、`UserCreateRequest.java`、`UserController.java`

Spring Boot 3 底层升级到 Jakarta EE 9+，**所有 `javax.*` 包名替换为 `jakarta.*`**。

| SP2 包名 | SP3 包名 |
|----------|----------|
| `javax.persistence.*` | `jakarta.persistence.*` |
| `javax.validation.*` | `jakarta.validation.*` |
| `javax.servlet.*` | `jakarta.servlet.*` |

**代码对比**：

```java
// SP2
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

// SP3
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
```

**影响范围**：所有使用 JPA 注解（`@Entity`, `@Table`, `@Id`, `@Column` 等）和 Bean Validation 注解（`@NotBlank`, `@Email`, `@Valid` 等）的文件。

**迁移工具**：IntelliJ IDEA 提供批量替换功能：`Refactor > Migrate Packages from javax to jakarta`。

---

### 5.2 SecurityFilterChain（安全配置方式重写）

**代码位置**：`config/SecurityConfig.java`

这是 SP3 中变化最大的配置之一。SP2 的 `WebSecurityConfigurerAdapter` 已被移除，改为定义 `SecurityFilterChain` Bean。

#### SP2 写法（已废弃）

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
            .and().formLogin();
    }
}
```

#### SP3 写法

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
            )
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())
        );

        return http.build();
    }
}
```

**关键 API 变化**：

| SP2 API | SP3 API | 说明 |
|---------|---------|------|
| `extends WebSecurityConfigurerAdapter` | 定义 `@Bean SecurityFilterChain` | 适配器类已移除 |
| `authorizeRequests()` | `authorizeHttpRequests()` | 方法名更精确 |
| `antMatchers(...)` | `requestMatchers(...)` | 方法名统一 |
| `.and().formLogin()` | Lambda DSL 内直接配置 | 不再需要 `.and()` 链 |
| `WebSecurityConfigurerAdapter.configure()` | `SecurityFilterChain` Bean | 组合优于继承 |

---

### 5.3 RestClient（新的同步 HTTP 客户端）

**代码位置**：`http/ExternalApiClient.java`

SP3 新增了 `RestClient`，定位是 `RestTemplate` 的现代化替代。它拥有 `WebClient` 一样的流式 API 风格，但保持同步阻塞模型（绝大多数项目不需要响应式）。

#### SP2 做法

```java
// RestTemplate — 代码冗长
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<Map> response = restTemplate.getForEntity(
    "https://api.example.com/posts/{id}", Map.class, id);
Map body = response.getBody();

// WebClient — 需要 WebFlux 依赖
WebClient client = WebClient.create("https://api.example.com");
Map body = client.get().uri("/posts/{id}", id)
    .retrieve().bodyToMono(Map.class).block();
```

#### SP3 写法

```java
@Component
public class ExternalApiClient {

    private final RestClient restClient;

    public ExternalApiClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    // GET 请求
    public Map<String, Object> getPost(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Map.class);
    }

    // POST 请求
    public Map<String, Object> createPost(String title, String body) {
        Map<String, Object> requestBody = Map.of(
                "title", title, "body", body, "userId", 1
        );
        return restClient.post()
                .uri("/posts")
                .body(requestBody)
                .retrieve()
                .body(Map.class);
    }

    // 带错误处理
    public ApiResponse<Map<String, Object>> getPostSafe(Long id) {
        try {
            Map<String, Object> result = restClient.get()
                    .uri("/posts/{id}", id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(),
                        (request, response) -> {
                            log.warn("客户端错误: {}", response.getStatusCode());
                        })
                    .body(Map.class);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("外部API调用失败: " + e.getMessage());
        }
    }
}
```

**对比总结**：

| 特性 | RestTemplate | WebClient | RestClient (SP3) |
|------|-------------|-----------|-----------------|
| 编程模型 | 同步阻塞 | 响应式 | 同步阻塞 |
| API 风格 | 命令式 | 流式 | 流式 |
| 依赖 | 无额外依赖 | 需要 WebFlux | 无额外依赖 |
| 推荐度 | 已不推荐 | 响应式场景 | **SP3 默认推荐** |

---

### 5.4 AutoConfiguration.imports（自动配置注册方式变更）

**代码位置**：
- 注册文件：`src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- 自动配置类：`autoconfig/AuditAutoConfiguration.java`

#### SP2 方式

在 `META-INF/spring.factories` 中注册：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.demo.autoconfig.AuditAutoConfiguration
```

#### SP3 方式

在 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中直接写全限定类名：

```
com.example.demo.autoconfig.AuditAutoConfiguration
```

**为什么要改**：
- `spring.factories` 是全局文件，所有自动配置挤在一起
- `imports` 文件专用于自动配置，职责更清晰
- SP3 启动速度更快（按需加载 `imports` 而非扫描整个 `spring.factories`）

**代码示例**（自动配置类使用 `@AutoConfiguration` 替代 `@Configuration`）：

```java
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "app",
        name = "audit-enabled",
        havingValue = "true",
        matchIfMissing = true  // 默认启用
)
public class AuditAutoConfiguration {

    @Bean
    public AuditService auditService() {
        log.info(">>> 审计服务自动配置已启用");
        return new AuditService();
    }
}
```

---

### 5.5 @ConfigurationProperties（配置属性绑定）

**代码位置**：`config/AppConfig.java`

SP3 推荐使用 `@ConfigurationProperties` 替代 `@Value` 来绑定配置，支持松散绑定和类型安全。

```java
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String name = "JDK17 SP3 Scaffold";
    private String version = "1.0.0";
    private boolean auditEnabled = true;
    private int defaultPageSize = 10;
    private String jwtSecret = "change-me-in-production";
}
```

对应的 `application.yml`：

```yaml
app:
  name: JDK17 SP3 Scaffold
  version: 1.0.0
  audit-enabled: true       # 松散绑定：audit-enabled = auditEnabled = audit_enabled
  default-page-size: 10
  jwt-secret: dev-only-secret-change-in-prod
```

**与 `@Value` 的对比**：

| 对比项 | @Value | @ConfigurationProperties |
|--------|--------|------------------------|
| 绑定方式 | 单个字段 | 批量绑定整个前缀 |
| 松散绑定 | 不支持 | 支持 |
| 类型安全 | 弱（String 拼 SpEL） | 强（直接映射类型） |
| 元数据 | 无 | 生成 spring-configuration-metadata.json |
| 推荐度 | 简单场景 | **SP3 推荐** |

---

## 6. 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+
- （可选）IntelliJ IDEA 2023+

### 运行步骤

```bash
# 1. 克隆项目
cd javaweb

# 2. 编译
mvn clean compile

# 3. 运行（默认激活 dev 环境）
mvn spring-boot:run

# 或指定环境
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# 4. 运行测试
mvn test
```

### 验证启动

启动成功后访问：

| 地址 | 说明 |
|------|------|
| http://localhost:8080/api/users | 用户列表（需认证） |
| http://localhost:8080/actuator/health | 健康检查 |
| http://localhost:8080/h2-console | H2 数据库控制台（dev 环境） |

H2 控制台连接参数：
- JDBC URL: `jdbc:h2:mem:devdb`
- User Name: `sa`
- Password: （留空）

---

## 7. API 接口文档

### 7.1 认证接口 — `/api/auth`

| 方法 | 端点 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/login` | 用户登录 | 不需要 |

**POST /api/auth/login**

请求体（JSON）：

```json
{
  "username": "admin",
  "password": "admin123"
}
```

成功响应（200）：

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "demo-jwt-token-1713000000000",
    "username": "admin"
  }
}
```

失败响应（401）：

```json
{
  "code": 401,
  "message": "用户名或密码错误",
  "data": null
}
```

---

### 7.2 用户接口 — `/api/users`

| 方法 | 端点 | 说明 | 认证 | 参数 |
|------|------|------|------|------|
| POST | `/api/users` | 创建用户 | 需要 | 请求体：`UserCreateRequest` |
| GET | `/api/users/{id}` | 查询单个用户 | 需要 | 路径参数：`id`（Long） |
| GET | `/api/users` | 分页查询用户列表 | 需要 | 查询参数：`page`, `size`, `sortBy`, `sortDir` |
| PUT | `/api/users/{id}` | 更新用户 | 需要 | 路径参数：`id`，请求体：`UserUpdateRequest` |
| DELETE | `/api/users/{id}` | 删除用户 | 需要 | 路径参数：`id`（Long） |

**POST /api/users — 创建用户**

请求体：

```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "123456",
  "displayName": "新用户"
}
```

字段说明：

| 字段 | 类型 | 必填 | 校验规则 |
|------|------|------|----------|
| `username` | String | 是 | 不能为空，3-50 字符 |
| `email` | String | 是 | 不能为空，邮箱格式 |
| `password` | String | 是 | 不能为空，6-100 字符 |
| `displayName` | String | 否 | 无校验 |

成功响应（200）：

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 3,
    "username": "newuser",
    "email": "newuser@example.com",
    "displayName": "新用户",
    "phone": null,
    "status": "ACTIVE",
    "roles": [],
    "createdAt": "2026-04-15 10:30:00"
  }
}
```

**GET /api/users/{id} — 查询单个用户**

成功响应（200）：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "displayName": "管理员",
    "phone": null,
    "status": "ACTIVE",
    "roles": ["ROLE_ADMIN", "ROLE_USER"],
    "createdAt": "2026-04-15 10:00:00"
  }
}
```

**GET /api/users — 分页查询**

查询参数：

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `page` | int | 0 | 页码（从 0 开始） |
| `size` | int | 10 | 每页大小（1-100） |
| `sortBy` | String | createdAt | 排序字段 |
| `sortDir` | String | desc | 排序方向（asc/desc） |

示例：`GET /api/users?page=0&size=5&sortBy=username&sortDir=asc`

**PUT /api/users/{id} — 更新用户**

请求体（所有字段可选，null 表示不更新）：

```json
{
  "email": "newemail@example.com",
  "displayName": "更新的名字",
  "phone": "13800138000"
}
```

**DELETE /api/users/{id} — 删除用户**

成功响应（200）：

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 7.3 公开端点（无需认证）

| 端点 | 说明 |
|------|------|
| `/api/auth/**` | 认证相关 |
| `/h2-console/**` | H2 数据库控制台 |
| `/actuator/health` | 健康检查 |

### 7.4 管理端点（需 ADMIN 角色）

| 端点 | 说明 |
|------|------|
| `/actuator/**` | 所有 Actuator 端点（除 health） |
| `/actuator/info` | 应用信息 |
| `/actuator/metrics` | 性能指标 |
| `/actuator/env` | 环境变量 |

---

## 8. 配置说明

### 8.1 application.yml（通用配置）

```yaml
# 激活的环境配置文件
spring:
  profiles:
    active: dev          # 切换为 prod 使用生产配置

  # Jackson 序列化配置
  jackson:
    default-property-inclusion: non_null   # null 字段不参与序列化
    date-format: yyyy-MM-dd HH:mm:ss      # 日期格式
    time-zone: Asia/Shanghai               # 时区

# Actuator 监控端点
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,env   # 暴露的端点
  endpoint:
    health:
      show-details: when-authorized        # 仅授权用户可见详情

# 日志级别
logging:
  level:
    root: INFO                             # 根日志级别
    com.example.demo: DEBUG                # 项目代码 DEBUG
    org.springframework.security: INFO     # Security INFO

# 自定义配置（对应 AppConfig.java）
app:
  name: JDK17 SP3 Scaffold
  version: 1.0.0
  audit-enabled: true                      # 是否启用审计
  default-page-size: 10                    # 默认分页大小
  jwt-secret: dev-only-secret-change-in-prod
```

### 8.2 application-dev.yml（开发环境）

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devdb                 # H2 内存数据库
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create-drop                # 每次启动重建表
    show-sql: true                         # 打印 SQL
    properties:
      hibernate:
        format_sql: true                   # 格式化 SQL

  h2:
    console:
      enabled: true                        # 启用 H2 控制台
      path: /h2-console                    # 访问路径

  sql:
    init:
      mode: always                         # 每次启动执行 schema.sql + data.sql

server:
  port: 8080
```

### 8.3 application-prod.yml（生产环境）

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useSSL=true&serverTimezone=Asia/Shanghai
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: ${DB_USERNAME}               # 从环境变量读取
    password: ${DB_PASSWORD}               # 从环境变量读取
    hikari:
      maximum-pool-size: 20                # 连接池最大连接数
      minimum-idle: 5                      # 最小空闲连接

  jpa:
    hibernate:
      ddl-auto: validate                   # 只验证表结构，不修改
    show-sql: false                        # 不打印 SQL

  h2:
    console:
      enabled: false                       # 禁用 H2 控制台

  sql:
    init:
      mode: never                          # 不执行初始化脚本

server:
  port: 8080

logging:
  level:
    com.example.demo: INFO
```

### 配置对照表

| 配置项 | dev | prod | 说明 |
|--------|-----|------|------|
| 数据库 | H2 内存 | MySQL | 生产用真实数据库 |
| ddl-auto | create-drop | validate | 开发每次重建，生产只验证 |
| show-sql | true | false | 开发需要看 SQL |
| H2 Console | 启用 | 禁用 | 生产环境安全隐患 |
| 数据库密码 | 空 | 环境变量 | 生产绝不硬编码 |
| sql.init.mode | always | never | 生产用 Flyway/Liquibase |

---

## 9. 分层架构说明

本项目遵循经典的四层架构，每层职责单一，依赖方向自上而下：

```
┌─────────────────────────────────────────────┐
│                  Controller                  │  接收 HTTP 请求，返回响应
│         (AuthController, UserController)     │  参数校验（@Valid）
├─────────────────────────────────────────────┤
│                   Service                    │  业务逻辑
│            (UserService → Impl)             │  事务管理（@Transactional）
├─────────────────────────────────────────────┤
│                 Repository                   │  数据访问
│       (UserRepository, RoleRepository)       │  Spring Data JPA 接口
├─────────────────────────────────────────────┤
│           Entity / Model / DTO               │  数据模型
│  (User, Role, BaseEntity, ApiResponse...)    │  JPA 实体 + Record DTO
└─────────────────────────────────────────────┘
```

### Controller 层

**职责**：接收 HTTP 请求，调用 Service，返回统一格式的 `ApiResponse`。

**特点**：
- 使用 `@RestController` + `@RequestMapping` 定义端点
- 请求参数使用 Record DTO + `@Valid` 校验
- 不包含业务逻辑，只做转发和响应封装
- 使用 Lombok `@RequiredArgsConstructor` 做构造器注入

**代码示例**（`UserController.java`）：

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return ApiResponse.success("创建成功", user);
    }
}
```

### Service 层

**职责**：实现业务逻辑，管理事务。

**特点**：
- 接口 + 实现分离（`UserService` 接口 + `UserServiceImpl` 实现）
- 使用 `@Transactional` 声明式事务
- 构造器注入 Repository（`@RequiredArgsConstructor`）
- 参数校验、业务规则判断在此层处理

**代码示例**（`UserServiceImpl.java`）：

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("用户名已存在: " + request.username());
        }
        // ... 创建逻辑
    }
}
```

### Repository 层

**职责**：数据访问，与数据库交互。

**特点**：
- 继承 `JpaRepository<Entity, Long>` 获得基本 CRUD
- 支持方法名派生查询（Derived Query Methods）
- 支持 `@Query` 自定义 JPQL
- 返回 `Optional<T>` 避免 NPE

**代码示例**（`UserRepository.java`）：

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.status = :status ORDER BY u.createdAt DESC")
    List<User> findByStatus(@Param("status") AccountStatus status);
}
```

### Entity / Model 层

**职责**：定义数据模型，包括 JPA 实体和 DTO。

**组成**：
- `BaseEntity` — 实体基类，包含 `id`、`createdAt`、`updatedAt`、`version`（乐观锁）
- `User` — 用户实体，JPA 注解定义表映射
- `Role` — 角色实体，与 User 多对多关系
- `AccountStatus` — 账户状态枚举
- `ApiResponse<T>` — Record 类型的统一响应封装
- 各种 Request/Response Record DTO

**JPA 实体示例**（`User.java`）：

```java
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Enumerated(EnumType.STRING)       // 用字符串存储枚举，更安全
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
```

### 全局异常处理

`GlobalExceptionHandler` 使用 `@RestControllerAdvice` 统一处理异常，返回标准 `ApiResponse` 格式：

| 异常类型 | HTTP 状态码 | 说明 |
|----------|-----------|------|
| `ResourceNotFoundException` | 404 | 资源不存在 |
| `BusinessException` | 400 | 业务规则违反 |
| `MethodArgumentNotValidException` | 400 | 参数校验失败 |
| `Exception` | 500 | 未知异常兜底 |

---

## 10. 测试说明

### 测试框架

- **JUnit 5**（SP3 默认）：`@Test`、`@DisplayName`、`@ParameterizedTest`
- **Spring Boot Test**：`@SpringBootTest`、`MockMvc`
- **Spring Security Test**：`@WithMockUser`

### 现有测试

**文件**：`src/test/java/com/example/demo/util/PatternMatchingDemoTest.java`

覆盖 JDK 17 特性的单元测试，共 12 个测试用例：

| 测试类别 | 测试方法 | 验证内容 |
|----------|---------|----------|
| Pattern Matching | `describeObject_string` | 字符串类型识别 |
| Pattern Matching | `describeObject_integer` | 整数类型识别 |
| Pattern Matching | `describeObject_null` | null 值处理 |
| Pattern Matching | `describeObject_unknown` | 未知类型处理 |
| Switch 表达式 | `levelToString` | 等级映射（1→LOW, 2→MEDIUM, 3→HIGH） |
| Switch 表达式 | `dayType_workday` | 工作日判断 |
| Switch 表达式 | `dayType_weekend` | 周末判断 |
| Switch 表达式 | `dayType_invalid` | 无效输入抛异常 |
| Text Block | `buildHtmlTemplate` | HTML 模板内容验证 |
| Text Block | `buildSelectQuery` | SQL 查询格式验证 |
| Sealed Class | `circle_area` | 圆形面积计算 |
| Sealed Class | `describeShape_*` | switch 穷尽检查（圆形/矩形/三角形） |

### 运行测试

```bash
# 运行全部测试
mvn test

# 运行单个测试类
mvn test -Dtest=PatternMatchingDemoTest

# 运行单个测试方法
mvn test -Dtest=PatternMatchingDemoTest#describeObject_string
```

### 测试编写建议

**推荐的测试结构**（已在测试中遵循）：

```java
@DisplayName("JDK 17 新特性测试")
class PatternMatchingDemoTest {

    @Test
    @DisplayName("Pattern Matching: 识别字符串类型")
    void describeObject_string() {
        // Arrange（准备）
        // Act（执行）
        String result = PatternMatchingDemo.describeObject("hello");
        // Assert（断言）
        assertEquals("字符串，长度=5", result);
    }
}
```

**扩展测试建议**：
- 使用 `MockMvc` 测试 Controller 端点
- 使用 `@MockBean` 模拟 Service 层
- 使用 Testcontainers 做数据库集成测试
- 目标覆盖率 80%+

---

## 附录：SP2 → SP3 迁移速查表

| 变更项 | SP2 | SP3 | 本项目代码位置 |
|--------|-----|-----|--------------|
| JDK 版本 | 8+ | 17+（最低要求） | `pom.xml` |
| 包名 | `javax.*` | `jakarta.*` | 所有 Entity / DTO |
| 自动配置注册 | `spring.factories` | `AutoConfiguration.imports` | `resources/META-INF/spring/` |
| 自动配置注解 | `@Configuration` | `@AutoConfiguration` | `AuditAutoConfiguration.java` |
| Security 配置 | `WebSecurityConfigurerAdapter` | `SecurityFilterChain` Bean | `SecurityConfig.java` |
| 路径匹配 | `antMatchers()` | `requestMatchers()` | `SecurityConfig.java` |
| HTTP 客户端 | `RestTemplate` | `RestClient` | `ExternalApiClient.java` |
| 配置绑定 | `@Value` | `@ConfigurationProperties` | `AppConfig.java` |
| 路径解析 | `AntPathMatcher` | `PathPatternParser`（默认） | 全局默认 |
