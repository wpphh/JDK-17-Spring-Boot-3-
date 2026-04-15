package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 3 启动类
 *
 * 与 SP2 的区别：
 * - SP3 底层基于 Spring Framework 6.x
 * - 自动装配注册方式从 spring.factories 改为 AutoConfiguration.imports
 * - 默认路径匹配策略从 AntPathMatcher 改为 PathPatternParser
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
