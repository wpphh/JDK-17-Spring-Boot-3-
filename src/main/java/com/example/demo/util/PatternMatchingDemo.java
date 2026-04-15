package com.example.demo.util;

/**
 * JDK 17 特性合集演示
 *
 * 包含：Pattern Matching for instanceof、Switch 表达式、Text Block、Sealed Class
 * 每个特性都有 JDK8 vs JDK17 的对比说明
 */
public class PatternMatchingDemo {

    // ================================================================
    // 特性 1：Pattern Matching for instanceof
    // ================================================================

    /**
     * 【JDK8 写法】
     * if (obj instanceof String) {
     *     String s = (String) obj;  // 必须手动强转
     *     return s.length();
     * }
     *
     * 【JDK17 写法】
     * if (obj instanceof String s) {  // 自动绑定变量 s，无需强转
     *     return s.length();
     * }
     *
     * 【JDK16+ 增强】还能直接加条件判断：
     * if (obj instanceof String s && s.length() > 5) { ... }
     */
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

    // ================================================================
    // 特性 2：Switch 表达式（JDK 14+ Preview，JDK 17 正式版）
    // ================================================================

    /**
     * 【JDK8 写法】
     * switch (level) {
     *     case 1: return "LOW";
     *     case 2: return "MEDIUM";
     *     case 3: return "HIGH";
     *     default: return "UNKNOWN";
     * }
     *
     * 【JDK17 写法 — 箭头语法】
     * - 不需要 break（不会 fall-through）
     * - 可以直接返回值（是表达式，不是语句）
     * - 多个 case 合并用逗号
     */
    public static String levelToString(int level) {
        return switch (level) {
            case 1 -> "LOW";
            case 2 -> "MEDIUM";
            case 3 -> "HIGH";
            default -> "UNKNOWN";
        };
    }

    /**
     * Switch 表达式 + yield — 当分支需要多行逻辑时用 yield 返回
     */
    public static String dayType(String day) {
        return switch (day.toLowerCase()) {
            case "monday", "tuesday", "wednesday", "thursday", "friday" -> {
                // yield 用于在代码块中返回值
                String result = day + " 是工作日";
                yield result;
            }
            case "saturday", "sunday" -> day + " 是周末";
            default -> throw new IllegalArgumentException("无效的星期: " + day);
        };
    }

    // ================================================================
    // 特性 3：Text Block 文本块（JDK 13+ Preview，JDK 15 正式版）
    // ================================================================

    /**
     * 【JDK8 写法】拼接 SQL/JSON/HTML 非常痛苦
     * String sql = "SELECT u.id, u.name, u.email "
     *            + "FROM users u "
     *            + "WHERE u.status = 'ACTIVE' "
     *            + "ORDER BY u.created_at DESC";
     *
     * 【JDK17 写法 — 三引号】
     * - 保留缩进和换行
     * - 不需要拼接符
     * - 支持嵌入变量（String.format 或 STR 模板）
     */
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

    // ================================================================
    // 特性 4：Sealed Class 演示
    // ================================================================

    /**
     * 密封接口 — 只允许指定的类实现
     * 这让编译器知道所有可能的实现类，从而实现穷尽检查
     */
    public sealed interface Shape permits Circle, Rectangle, Triangle {

        double area();
    }

    public record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }

    /**
     * Switch 表达式 + Sealed Class = 编译器穷尽检查
     * 如果你漏了一个子类型，编译直接报错！
     */
    public static String describeShape(Shape shape) {
        return switch (shape) {
            case Circle c -> "圆形，半径=" + c.radius() + "，面积=" + c.area();
            case Rectangle r -> "矩形，宽=" + r.width() + "，高=" + r.height() + "，面积=" + r.area();
            case Triangle t -> "三角形，底=" + t.base() + "，高=" + t.height() + "，面积=" + t.area();
            // 注意：这里不需要 default！因为编译器知道只有这3种可能
        };
    }
}
