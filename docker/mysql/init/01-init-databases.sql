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

-- 创建用户行为表
CREATE TABLE user_behaviors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    action_type VARCHAR(50) NOT NULL COMMENT '行为类型',
    target_id BIGINT NOT NULL COMMENT '目标对象ID',
    target_type VARCHAR(50) NOT NULL COMMENT '目标对象类型',
    duration INT DEFAULT 0 COMMENT '行为持续时间（秒）',
    rating INT COMMENT '行为评分（1-5分）',
    source VARCHAR(50) COMMENT '行为来源',
    device_type VARCHAR(20) COMMENT '设备类型',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    metadata JSON COMMENT '扩展数据',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_user_id (user_id),
    INDEX idx_action_type (action_type),
    INDEX idx_target (target_id, target_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为数据表';

-- 创建推荐结果表
CREATE TABLE recommendation_results (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '推荐的课程ID',
    algorithm_type VARCHAR(50) NOT NULL COMMENT '推荐算法类型',
    score DECIMAL(5,4) NOT NULL COMMENT '推荐分数',
    reason VARCHAR(200) COMMENT '推荐原因',
    position INT NOT NULL COMMENT '推荐位置',
    clicked BOOLEAN DEFAULT FALSE COMMENT '是否被点击',
    click_time TIMESTAMP NULL COMMENT '点击时间',
    converted BOOLEAN DEFAULT FALSE COMMENT '是否被转化',
    convert_time TIMESTAMP NULL COMMENT '转化时间',
    batch_id VARCHAR(50) NOT NULL COMMENT '推荐批次ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user_id (user_id),
    INDEX idx_course_id (course_id),
    INDEX idx_algorithm_type (algorithm_type),
    INDEX idx_batch_id (batch_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐结果表';

-- 创建直播间表
CREATE TABLE live_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '直播间标题',
    description TEXT COMMENT '直播间描述',
    teacher_id BIGINT NOT NULL COMMENT '主播用户ID',
    teacher_name VARCHAR(100) COMMENT '主播姓名',
    course_id BIGINT COMMENT '课程ID',
    cover_image VARCHAR(255) COMMENT '直播间封面图',
    status INT DEFAULT 0 COMMENT '直播状态：0-未开始，1-直播中，2-已结束',
    scheduled_start_time TIMESTAMP NULL COMMENT '预计开始时间',
    actual_start_time TIMESTAMP NULL COMMENT '实际开始时间',
    end_time TIMESTAMP NULL COMMENT '结束时间',
    current_viewers INT DEFAULT 0 COMMENT '当前观看人数',
    total_viewers INT DEFAULT 0 COMMENT '累计观看人数',
    max_viewers INT DEFAULT 0 COMMENT '最大观看人数',
    stream_url VARCHAR(255) COMMENT '直播流地址',
    play_url VARCHAR(255) COMMENT '播放地址',
    record_url VARCHAR(255) COMMENT '录制文件地址',
    chat_enabled BOOLEAN DEFAULT TRUE COMMENT '是否允许聊天',
    record_enabled BOOLEAN DEFAULT FALSE COMMENT '是否录制',
    password VARCHAR(50) COMMENT '直播间密码',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',

    INDEX idx_teacher_id (teacher_id),
    INDEX idx_course_id (course_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='直播间表';

-- 创建直播会话表
CREATE TABLE live_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id BIGINT NOT NULL COMMENT '直播间ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    nickname VARCHAR(100) COMMENT '用户昵称',
    role VARCHAR(20) DEFAULT 'student' COMMENT '用户角色：teacher-主播，student-学生',
    status VARCHAR(20) DEFAULT 'connected' COMMENT '会话状态：connected-已连接，disconnected-已断开',
    session_id VARCHAR(100) COMMENT 'WebSocket会话ID',
    ip_address VARCHAR(45) COMMENT '用户IP地址',
    user_agent TEXT COMMENT '用户代理',
    connect_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '连接时间',
    disconnect_time TIMESTAMP NULL COMMENT '断开时间',
    duration INT DEFAULT 0 COMMENT '在线时长（秒）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_room_id (room_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_connect_time (connect_time),
    FOREIGN KEY (room_id) REFERENCES live_room(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='直播会话表';

-- 创建学习分析数据表
CREATE TABLE learning_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    chapter_id BIGINT COMMENT '章节ID',
    action_type INT NOT NULL COMMENT '学习行为类型：1-观看视频，2-做练习，3-参与讨论，4-下载资料',
    duration INT DEFAULT 0 COMMENT '学习时长（秒）',
    progress DECIMAL(5,2) DEFAULT 0 COMMENT '学习进度（百分比）',
    score INT COMMENT '学习效果评分（1-5分）',
    device_type INT DEFAULT 1 COMMENT '设备类型：1-PC，2-手机，3-平板',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    location VARCHAR(100) COMMENT '地理位置',
    start_time TIMESTAMP COMMENT '学习开始时间',
    end_time TIMESTAMP COMMENT '学习结束时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除',

    INDEX idx_user_id (user_id),
    INDEX idx_course_id (course_id),
    INDEX idx_chapter_id (chapter_id),
    INDEX idx_action_type (action_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习分析数据表';

-- 创建课程分析数据表
CREATE TABLE course_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL COMMENT '课程ID',
    course_name VARCHAR(200) COMMENT '课程名称',
    stat_date TIMESTAMP NOT NULL COMMENT '统计日期',
    total_students INT DEFAULT 0 COMMENT '总学习人数',
    active_students INT DEFAULT 0 COMMENT '活跃学习人数',
    completed_students INT DEFAULT 0 COMMENT '完成人数',
    avg_learning_time DECIMAL(10,2) DEFAULT 0 COMMENT '平均学习时长（分钟）',
    avg_completion_rate DECIMAL(5,2) DEFAULT 0 COMMENT '平均完成率',
    avg_rating DECIMAL(3,2) DEFAULT 0 COMMENT '平均评分',
    total_learning_time BIGINT DEFAULT 0 COMMENT '总学习时长（分钟）',
    video_views INT DEFAULT 0 COMMENT '视频观看次数',
    exercise_completions INT DEFAULT 0 COMMENT '练习完成次数',
    discussion_participations INT DEFAULT 0 COMMENT '讨论参与次数',
    material_downloads INT DEFAULT 0 COMMENT '资料下载次数',
    dropout_students INT DEFAULT 0 COMMENT '退课人数',
    dropout_rate DECIMAL(5,2) DEFAULT 0 COMMENT '退课率',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_course_id (course_id),
    INDEX idx_stat_date (stat_date),
    UNIQUE KEY uk_course_date (course_id, stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分析数据表';

-- 创建用户学习统计表
CREATE TABLE user_learning_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    stat_date TIMESTAMP NOT NULL COMMENT '统计日期',
    learning_days INT DEFAULT 0 COMMENT '学习天数',
    total_learning_time BIGINT DEFAULT 0 COMMENT '总学习时长（分钟）',
    courses_learned INT DEFAULT 0 COMMENT '已学课程数',
    courses_completed INT DEFAULT 0 COMMENT '已完成课程数',
    videos_watched INT DEFAULT 0 COMMENT '观看视频数',
    exercises_completed INT DEFAULT 0 COMMENT '完成练习数',
    discussions_participated INT DEFAULT 0 COMMENT '参与讨论数',
    materials_downloaded INT DEFAULT 0 COMMENT '下载资料数',
    avg_daily_learning_time DECIMAL(10,2) DEFAULT 0 COMMENT '平均每日学习时长（分钟）',
    consecutive_days INT DEFAULT 0 COMMENT '学习连续天数',
    max_consecutive_days INT DEFAULT 0 COMMENT '最长连续学习天数',
    activity_score DECIMAL(3,2) DEFAULT 0 COMMENT '学习活跃度评分（1-5分）',
    effectiveness_score DECIMAL(3,2) DEFAULT 0 COMMENT '学习效果评分（1-5分）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user_id (user_id),
    INDEX idx_stat_date (stat_date),
    UNIQUE KEY uk_user_date (user_id, stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户学习统计表';

-- 使用Nacos配置数据库
USE nacos_config;
