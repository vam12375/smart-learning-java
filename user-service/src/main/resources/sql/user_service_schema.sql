-- 用户服务数据库表结构

-- 创建数据库
CREATE DATABASE IF NOT EXISTS smart_learning DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE smart_learning;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别 0:未知 1:男 2:女',
    birthday DATE COMMENT '生日',
    role VARCHAR(20) DEFAULT 'STUDENT' COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态 0:禁用 1:正常',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_role (role),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户资料表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    id_card VARCHAR(20) COMMENT '身份证号',
    education VARCHAR(50) COMMENT '学历',
    profession VARCHAR(100) COMMENT '职业',
    company VARCHAR(100) COMMENT '公司',
    bio TEXT COMMENT '个人简介',
    interests TEXT COMMENT '兴趣标签(JSON格式)',
    learning_goals TEXT COMMENT '学习目标',
    address VARCHAR(255) COMMENT '地址',
    wechat VARCHAR(50) COMMENT '微信号',
    qq VARCHAR(20) COMMENT 'QQ号',
    website VARCHAR(255) COMMENT '个人网站',
    github VARCHAR(255) COMMENT 'GitHub地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',

    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_real_name (real_name),
    INDEX idx_education (education),
    INDEX idx_profession (profession),
    INDEX idx_deleted (deleted),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户资料表';

-- 用户统计表
CREATE TABLE IF NOT EXISTS user_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_study_time INT DEFAULT 0 COMMENT '总学习时长(分钟)',
    enrolled_courses INT DEFAULT 0 COMMENT '已报名课程数',
    completed_courses INT DEFAULT 0 COMMENT '已完成课程数',
    completed_lessons INT DEFAULT 0 COMMENT '已完成课时数',
    note_count INT DEFAULT 0 COMMENT '学习笔记数',
    favorite_count INT DEFAULT 0 COMMENT '收藏课程数',
    exam_count INT DEFAULT 0 COMMENT '考试次数',
    exam_pass_count INT DEFAULT 0 COMMENT '考试通过次数',
    average_score DECIMAL(5,2) DEFAULT 0.00 COMMENT '平均考试分数',
    total_points INT DEFAULT 0 COMMENT '总积分',
    current_level INT DEFAULT 1 COMMENT '当前等级',
    continuous_days INT DEFAULT 0 COMMENT '连续学习天数',
    max_continuous_days INT DEFAULT 0 COMMENT '最长连续学习天数',
    last_study_time DATETIME COMMENT '最后学习时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',

    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_total_points (total_points),
    INDEX idx_current_level (current_level),
    INDEX idx_last_study_time (last_study_time),
    INDEX idx_deleted (deleted),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户统计表';

-- 用户登录日志表
CREATE TABLE IF NOT EXISTS user_login_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    login_ip VARCHAR(50) COMMENT '登录IP',
    user_agent TEXT COMMENT '用户代理',
    device_type VARCHAR(20) COMMENT '设备类型',
    location VARCHAR(100) COMMENT '登录地点',
    status TINYINT DEFAULT 1 COMMENT '登录状态 0:失败 1:成功',
    remark VARCHAR(255) COMMENT '备注',
    
    INDEX idx_user_id (user_id),
    INDEX idx_login_time (login_time),
    INDEX idx_login_ip (login_ip),
    INDEX idx_status (status),
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录日志表';

-- 插入默认管理员用户
INSERT INTO users (username, email, password, nickname, role, status) 
VALUES ('admin', 'admin@smartlearning.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOBSaEwHOhKKNjHgKw6', '系统管理员', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE username = username;

-- 插入测试用户
INSERT INTO users (username, email, password, nickname, role, status) 
VALUES ('student1', 'student1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOBSaEwHOhKKNjHgKw6', '学生1', 'STUDENT', 1)
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO users (username, email, password, nickname, role, status) 
VALUES ('teacher1', 'teacher1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOBSaEwHOhKKNjHgKw6', '教师1', 'TEACHER', 1)
ON DUPLICATE KEY UPDATE username = username;
