-- ============================================================
-- 开发环境初始数据
-- ============================================================

-- 角色数据
INSERT INTO roles (name, description) VALUES ('ROLE_ADMIN', '系统管理员');
INSERT INTO roles (name, description) VALUES ('ROLE_USER', '普通用户');

-- 管理员用户（密码: admin123，实际项目应用 BCrypt 加密）
INSERT INTO users (username, email, password, display_name, status)
VALUES ('admin', 'admin@example.com', '{noop}admin123', '管理员', 'ACTIVE');

-- 普通用户
INSERT INTO users (username, email, password, display_name, status)
VALUES ('user1', 'user1@example.com', '{noop}user123', '用户一', 'ACTIVE');

-- 分配角色
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
