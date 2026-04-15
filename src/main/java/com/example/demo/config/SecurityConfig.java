package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 6 配置 — SP3 最核心的变化之一
 *
 * ==========================================
 * 【SP2 → SP3 完全重写】
 * ==========================================
 *
 * 【SP2 写法 — 已废弃】
 * @Configuration
 * public class SecurityConfig extends WebSecurityConfigurerAdapter {
 *     @Override
 *     protected void configure(HttpSecurity http) throws Exception {
 *         http.authorizeRequests()
 *             .antMatchers("/api/public/**").permitAll()
 *             .anyRequest().authenticated()
 *             .and().formLogin();
 *     }
 * }
 *
 * 【SP3 写法 — 使用 Bean】
 * - WebSecurityConfigurerAdapter 已被移除
 * - 改为定义 SecurityFilterChain Bean
 * - authorizeRequests() → authorizeHttpRequests()
 * - antMatchers() → requestMatchers()
 * - and() 链式调用 → Lambda DSL（推荐）
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 核心 Bean — 定义安全过滤链
     * 这是 SP3 中配置安全的唯一推荐方式
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 使用 Lambda DSL 配置（SP3 推荐写法）
            // 不再需要 .and() 链式调用
            .csrf(csrf -> csrf
                // 开发环境禁用 CSRF（方便 API 测试）
                // 生产环境必须启用
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
            )

            // CORS 配置
            .cors(Customizer.withDefaults())

            // 路径授权规则
            .authorizeHttpRequests(auth -> auth
                // 公开端点 — 无需认证
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                // 管理端点 — 需要 ADMIN 角色
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                // 其余所有请求需要认证
                .anyRequest().authenticated()
            )

            // Session 策略 — 无状态（适合 REST API + JWT）
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 表单登录（开发用）
            .formLogin(Customizer.withDefaults())

            // HTTP Basic 认证（开发测试用）
            .httpBasic(Customizer.withDefaults());

        // H2 Console 需要允许 iframe
        http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())
        );

        return http.build();
    }

    /**
     * 密码编码器 — BCrypt
     * SP3 依然使用 BCrypt，但也可以选择 Argon2
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 内存用户 — 开发测试用
     * 实际项目从数据库加载用户（实现 UserDetailsService）
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        var user = User.builder()
                .username("user")
                .password(encoder.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
