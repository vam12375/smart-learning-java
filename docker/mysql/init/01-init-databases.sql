-- 创建智能学习平台数据库
CREATE DATABASE IF NOT EXISTS smart_learning DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建Nacos配置数据库
CREATE DATABASE IF NOT EXISTS nacos_config DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用智能学习平台数据库
USE smart_learning;

-- 创建用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    gender TINYINT COMMENT '性别 0:未知 1:男 2:女',
    birthday DATE COMMENT '生日',
    role ENUM('STUDENT', 'TEACHER', 'ADMIN') NOT NULL DEFAULT 'STUDENT' COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态 0:禁用 1:正常',
    last_login_time TIMESTAMP COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建用户资料表
CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    id_card VARCHAR(18) COMMENT '身份证号',
    education ENUM('PRIMARY', 'JUNIOR', 'SENIOR', 'BACHELOR', 'MASTER', 'DOCTOR') COMMENT '学历',
    profession VARCHAR(100) COMMENT '职业',
    company VARCHAR(200) COMMENT '公司',
    bio TEXT COMMENT '个人简介',
    interests JSON COMMENT '兴趣标签',
    learning_goals TEXT COMMENT '学习目标',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资料表';

-- 创建课程分类表
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    level TINYINT DEFAULT 1 COMMENT '分类层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    icon VARCHAR(255) COMMENT '图标',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';

-- 创建课程表
CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '课程标题',
    subtitle VARCHAR(300) COMMENT '副标题',
    description TEXT COMMENT '课程描述',
    teacher_id BIGINT NOT NULL COMMENT '讲师ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    cover_image VARCHAR(255) COMMENT '封面图片',
    trailer_video VARCHAR(255) COMMENT '预览视频',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '价格',
    original_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '原价',
    difficulty ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER' COMMENT '难度',
    duration INT DEFAULT 0 COMMENT '总时长(分钟)',
    lesson_count INT DEFAULT 0 COMMENT '课时数',
    student_count INT DEFAULT 0 COMMENT '学生数',
    rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分',
    rating_count INT DEFAULT 0 COMMENT '评分人数',
    tags JSON COMMENT '标签',
    requirements TEXT COMMENT '学习要求',
    objectives TEXT COMMENT '学习目标',
    target_audience TEXT COMMENT '目标学员',
    language VARCHAR(20) DEFAULT 'zh-CN' COMMENT '语言',
    status ENUM('DRAFT', 'REVIEWING', 'PUBLISHED', 'OFFLINE') DEFAULT 'DRAFT' COMMENT '状态',
    is_free BOOLEAN DEFAULT FALSE COMMENT '是否免费',
    is_featured BOOLEAN DEFAULT FALSE COMMENT '是否推荐',
    published_at TIMESTAMP NULL COMMENT '发布时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (teacher_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_teacher (teacher_id),
    INDEX idx_category (category_id),
    INDEX idx_status (status),
    INDEX idx_price (price),
    INDEX idx_rating (rating),
    INDEX idx_published_at (published_at),
    FULLTEXT idx_title_desc (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 插入初始数据
INSERT INTO categories (name, parent_id, level, sort_order, description) VALUES
('编程语言', 0, 1, 1, '各种编程语言学习'),
('前端开发', 0, 1, 2, '前端技术栈学习'),
('后端开发', 0, 1, 3, '后端技术栈学习'),
('数据库', 0, 1, 4, '数据库技术学习'),
('人工智能', 0, 1, 5, 'AI相关技术学习');

-- 插入管理员用户
INSERT INTO users (username, email, password, nickname, role, status) VALUES
('admin', 'admin@smartlearning.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLO.TAGxKjdS', '系统管理员', 'ADMIN', 1);

-- 使用Nacos配置数据库
USE nacos_config;
