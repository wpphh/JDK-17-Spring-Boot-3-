package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JDK 17 特性测试
 *
 * 使用 JUnit 5（SP3 默认测试框架）
 */
@DisplayName("JDK 17 新特性测试")
class PatternMatchingDemoTest {

    // ================================================================
    // Pattern Matching for instanceof 测试
    // ================================================================

    @Test
    @DisplayName("Pattern Matching: 识别字符串类型")
    void describeObject_string() {
        String result = PatternMatchingDemo.describeObject("hello");
        assertEquals("字符串，长度=5", result);
    }

    @Test
    @DisplayName("Pattern Matching: 识别整数类型")
    void describeObject_integer() {
        String result = PatternMatchingDemo.describeObject(42);
        assertEquals("整数，值=42", result);
    }

    @Test
    @DisplayName("Pattern Matching: null 值处理")
    void describeObject_null() {
        String result = PatternMatchingDemo.describeObject(null);
        assertEquals("null", result);
    }

    @Test
    @DisplayName("Pattern Matching: 未知类型")
    void describeObject_unknown() {
        String result = PatternMatchingDemo.describeObject(new Object());
        assertTrue(result.startsWith("未知类型"));
    }

    // ================================================================
    // Switch 表达式测试
    // ================================================================

    @Test
    @DisplayName("Switch 表达式: 等级映射")
    void levelToString() {
        assertEquals("LOW", PatternMatchingDemo.levelToString(1));
        assertEquals("MEDIUM", PatternMatchingDemo.levelToString(2));
        assertEquals("HIGH", PatternMatchingDemo.levelToString(3));
        assertEquals("UNKNOWN", PatternMatchingDemo.levelToString(99));
    }

    @Test
    @DisplayName("Switch 表达式: 工作日判断")
    void dayType_workday() {
        assertEquals("monday 是工作日", PatternMatchingDemo.dayType("monday"));
    }

    @Test
    @DisplayName("Switch 表达式: 周末判断")
    void dayType_weekend() {
        assertEquals("saturday 是周末", PatternMatchingDemo.dayType("saturday"));
    }

    @Test
    @DisplayName("Switch 表达式: 无效输入抛异常")
    void dayType_invalid() {
        assertThrows(IllegalArgumentException.class, () -> PatternMatchingDemo.dayType("xxx"));
    }

    // ================================================================
    // Text Block 测试
    // ================================================================

    @Test
    @DisplayName("Text Block: HTML 模板包含正确内容")
    void buildHtmlTemplate() {
        String html = PatternMatchingDemo.buildHtmlTemplate();
        assertTrue(html.contains("<title>Demo Page</title>"));
        assertTrue(html.contains("<h1>欢迎</h1>"));
    }

    @Test
    @DisplayName("Text Block: SQL 查询格式正确")
    void buildSelectQuery() {
        String sql = PatternMatchingDemo.buildSelectQuery("users");
        assertTrue(sql.contains("FROM users"));
        assertTrue(sql.contains("WHERE status = 'ACTIVE'"));
    }

    // ================================================================
    // Sealed Class + Record 测试
    // ================================================================

    @Test
    @DisplayName("Sealed Class: 圆形面积计算")
    void circle_area() {
        var circle = new PatternMatchingDemo.Circle(5);
        assertEquals(Math.PI * 25, circle.area(), 0.001);
    }

    @Test
    @DisplayName("Sealed Class: 矩形面积计算")
    void rectangle_area() {
        var rect = new PatternMatchingDemo.Rectangle(4, 6);
        assertEquals(24.0, rect.area());
    }

    @Test
    @DisplayName("Sealed Class: switch 穷尽检查 — 圆形")
    void describeShape_circle() {
        var shape = new PatternMatchingDemo.Circle(3);
        String result = PatternMatchingDemo.describeShape(shape);
        assertTrue(result.contains("圆形"));
        assertTrue(result.contains("半径=3.0"));
    }

    @Test
    @DisplayName("Sealed Class: switch 穷尽检查 — 矩形")
    void describeShape_rectangle() {
        var shape = new PatternMatchingDemo.Rectangle(5, 10);
        String result = PatternMatchingDemo.describeShape(shape);
        assertTrue(result.contains("矩形"));
    }

    @Test
    @DisplayName("Sealed Class: switch 穷尽检查 — 三角形")
    void describeShape_triangle() {
        var shape = new PatternMatchingDemo.Triangle(6, 4);
        String result = PatternMatchingDemo.describeShape(shape);
        assertTrue(result.contains("三角形"));
        assertTrue(result.contains("面积=12.0"));
    }
}
