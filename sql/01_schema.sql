-- ================================================
-- 智能学习平台数据库设计
-- Database: smart_learning
-- Version: 1.0.0
-- Date: 2025-08-29
-- ================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS smart_learning CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smart_learning;

-- ================================================
-- 用户服务相关表
-- ================================================

-- 用户基础表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    birthday DATE COMMENT '生日',
    role VARCHAR(20) DEFAULT 'STUDENT' COMMENT '角色：STUDENT-学生，TEACHER-教师，ADMIN-管理员',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户详细信息表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    id_card VARCHAR(20) COMMENT '身份证号',
    education VARCHAR(20) COMMENT '学历',
    school VARCHAR(100) COMMENT '学校',
    major VARCHAR(100) COMMENT '专业',
    company VARCHAR(100) COMMENT '公司',
    position VARCHAR(50) COMMENT '职位',
    bio TEXT COMMENT '个人简介',
    interests TEXT COMMENT '兴趣爱好',
    city VARCHAR(50) COMMENT '城市',
    address VARCHAR(200) COMMENT '详细地址',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户详情表';

-- 用户统计表
CREATE TABLE IF NOT EXISTS user_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    total_study_time INT DEFAULT 0 COMMENT '总学习时长（分钟）',
    total_courses INT DEFAULT 0 COMMENT '总课程数',
    completed_courses INT DEFAULT 0 COMMENT '完成课程数',
    total_exams INT DEFAULT 0 COMMENT '总考试数',
    passed_exams INT DEFAULT 0 COMMENT '通过考试数',
    total_credits INT DEFAULT 0 COMMENT '总学分',
    ranking INT DEFAULT 0 COMMENT '排名',
    points INT DEFAULT 0 COMMENT '积分',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_ranking (ranking)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户统计表';

-- ================================================
-- 课程服务相关表
-- ================================================

-- 课程分类表
CREATE TABLE IF NOT EXISTS course_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程分类表';

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '课程标题',
    subtitle VARCHAR(300) COMMENT '副标题',
    description TEXT COMMENT '课程描述',
    cover_image VARCHAR(500) COMMENT '封面图片',
    category_id BIGINT COMMENT '分类ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    price DECIMAL(10,2) DEFAULT 0 COMMENT '价格',
    original_price DECIMAL(10,2) DEFAULT 0 COMMENT '原价',
    difficulty VARCHAR(20) DEFAULT 'BEGINNER' COMMENT '难度：BEGINNER-初级，INTERMEDIATE-中级，ADVANCED-高级',
    duration INT DEFAULT 0 COMMENT '总时长（分钟）',
    lesson_count INT DEFAULT 0 COMMENT '课时数',
    student_count INT DEFAULT 0 COMMENT '学生数',
    favorite_count INT DEFAULT 0 COMMENT '收藏数',
    rating DECIMAL(3,2) DEFAULT 0 COMMENT '评分',
    rating_count INT DEFAULT 0 COMMENT '评价数',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布，OFFLINE-下线',
    is_free TINYINT DEFAULT 0 COMMENT '是否免费',
    is_featured TINYINT DEFAULT 0 COMMENT '是否推荐',
    tags VARCHAR(500) COMMENT '标签（逗号分隔）',
    requirements TEXT COMMENT '学习要求',
    objectives TEXT COMMENT '学习目标',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published_at DATETIME COMMENT '发布时间',
    FOREIGN KEY (category_id) REFERENCES course_categories(id) ON DELETE SET NULL,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_category_id (category_id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    INDEX idx_price (price),
    INDEX idx_student_count (student_count),
    INDEX idx_rating (rating),
    FULLTEXT idx_fulltext (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- 章节表
CREATE TABLE IF NOT EXISTS course_chapters (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL COMMENT '课程ID',
    title VARCHAR(200) NOT NULL COMMENT '章节标题',
    description TEXT COMMENT '章节描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    duration INT DEFAULT 0 COMMENT '时长（分钟）',
    lesson_count INT DEFAULT 0 COMMENT '课时数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程章节表';

-- 课时表
CREATE TABLE IF NOT EXISTS course_lessons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    chapter_id BIGINT NOT NULL COMMENT '章节ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    title VARCHAR(200) NOT NULL COMMENT '课时标题',
    description TEXT COMMENT '课时描述',
    type VARCHAR(20) DEFAULT 'VIDEO' COMMENT '类型：VIDEO-视频，DOCUMENT-文档，QUIZ-测验',
    video_url VARCHAR(500) COMMENT '视频URL',
    video_duration INT DEFAULT 0 COMMENT '视频时长（秒）',
    document_url VARCHAR(500) COMMENT '文档URL',
    sort_order INT DEFAULT 0 COMMENT '排序',
    is_free TINYINT DEFAULT 0 COMMENT '是否免费试看',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES course_chapters(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_chapter_id (chapter_id),
    INDEX idx_course_id (course_id),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程课时表';

-- ================================================
-- 学习服务相关表
-- ================================================

-- 学习进度表
CREATE TABLE IF NOT EXISTS learning_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    lesson_id BIGINT COMMENT '当前课时ID',
    total_lessons INT DEFAULT 0 COMMENT '总课时数',
    completed_lessons INT DEFAULT 0 COMMENT '已完成课时数',
    progress DECIMAL(5,2) DEFAULT 0 COMMENT '进度百分比',
    total_duration INT DEFAULT 0 COMMENT '总学习时长（秒）',
    last_position INT DEFAULT 0 COMMENT '最后播放位置（秒）',
    last_study_time DATETIME COMMENT '最后学习时间',
    status VARCHAR(20) DEFAULT 'LEARNING' COMMENT '状态：LEARNING-学习中，COMPLETED-已完成',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_course (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_course_id (course_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习进度表';

-- 学习记录表
CREATE TABLE IF NOT EXISTS learning_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    lesson_id BIGINT NOT NULL COMMENT '课时ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    duration INT DEFAULT 0 COMMENT '学习时长（秒）',
    progress INT DEFAULT 0 COMMENT '进度（秒）',
    is_completed TINYINT DEFAULT 0 COMMENT '是否完成',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES course_lessons(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_course_id (course_id),
    INDEX idx_lesson_id (lesson_id),
    INDEX idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习记录表';

-- 学习笔记表
CREATE TABLE IF NOT EXISTS learning_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    lesson_id BIGINT COMMENT '课时ID',
    title VARCHAR(200) COMMENT '笔记标题',
    content TEXT NOT NULL COMMENT '笔记内容',
    video_time INT COMMENT '视频时间点（秒）',
    is_public TINYINT DEFAULT 0 COMMENT '是否公开',
    likes INT DEFAULT 0 COMMENT '点赞数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES course_lessons(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_course_id (course_id),
    INDEX idx_lesson_id (lesson_id),
    FULLTEXT idx_content (content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习笔记表';

-- 课程收藏表
CREATE TABLE IF NOT EXISTS course_favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_course (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程收藏表';

-- ================================================
-- 考试服务相关表
-- ================================================

-- 题库表
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT COMMENT '所属课程ID',
    chapter_id BIGINT COMMENT '所属章节ID',
    type VARCHAR(20) NOT NULL COMMENT '题型：SINGLE-单选，MULTIPLE-多选，JUDGE-判断，FILL-填空，ESSAY-问答',
    difficulty VARCHAR(20) DEFAULT 'EASY' COMMENT '难度：EASY-简单，MEDIUM-中等，HARD-困难',
    title TEXT NOT NULL COMMENT '题目',
    options JSON COMMENT '选项（JSON格式）',
    answer TEXT NOT NULL COMMENT '答案',
    analysis TEXT COMMENT '解析',
    score INT DEFAULT 1 COMMENT '分值',
    tags VARCHAR(500) COMMENT '标签',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
    FOREIGN KEY (chapter_id) REFERENCES course_chapters(id) ON DELETE SET NULL,
    INDEX idx_course_id (course_id),
    INDEX idx_chapter_id (chapter_id),
    INDEX idx_type (type),
    INDEX idx_difficulty (difficulty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题库表';

-- 试卷表
CREATE TABLE IF NOT EXISTS exam_papers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '试卷标题',
    description TEXT COMMENT '试卷描述',
    course_id BIGINT COMMENT '所属课程ID',
    type VARCHAR(20) DEFAULT 'PRACTICE' COMMENT '类型：PRACTICE-练习，EXAM-考试，QUIZ-测验',
    total_score INT DEFAULT 100 COMMENT '总分',
    pass_score INT DEFAULT 60 COMMENT '及格分',
    time_limit INT DEFAULT 120 COMMENT '限时（分钟）',
    question_count INT DEFAULT 0 COMMENT '题目数量',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布，CLOSED-已关闭',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    created_by BIGINT COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
    INDEX idx_course_id (course_id),
    INDEX idx_type (type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷表';

-- 试卷题目关联表
CREATE TABLE IF NOT EXISTS exam_paper_questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    paper_id BIGINT NOT NULL COMMENT '试卷ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    score INT COMMENT '题目分值',
    FOREIGN KEY (paper_id) REFERENCES exam_papers(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    UNIQUE KEY uk_paper_question (paper_id, question_id),
    INDEX idx_paper_id (paper_id),
    INDEX idx_question_id (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷题目关联表';

-- 考试记录表
CREATE TABLE IF NOT EXISTS exam_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    paper_id BIGINT NOT NULL COMMENT '试卷ID',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    submit_time DATETIME COMMENT '提交时间',
    time_used INT COMMENT '用时（秒）',
    score DECIMAL(5,2) COMMENT '得分',
    status VARCHAR(20) DEFAULT 'IN_PROGRESS' COMMENT '状态：IN_PROGRESS-进行中，SUBMITTED-已提交，MARKED-已批改',
    is_passed TINYINT COMMENT '是否通过',
    answers JSON COMMENT '答案（JSON格式）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (paper_id) REFERENCES exam_papers(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_paper_id (paper_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试记录表';

-- ================================================
-- 直播服务相关表
-- ================================================

-- 直播间表
CREATE TABLE IF NOT EXISTS live_rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL COMMENT '直播标题',
    description TEXT COMMENT '直播描述',
    cover_image VARCHAR(500) COMMENT '封面图片',
    course_id BIGINT COMMENT '关联课程ID',
    teacher_id BIGINT NOT NULL COMMENT '主播ID',
    room_key VARCHAR(100) UNIQUE COMMENT '房间密钥',
    stream_url VARCHAR(500) COMMENT '推流地址',
    play_url VARCHAR(500) COMMENT '播放地址',
    status VARCHAR(20) DEFAULT 'CREATED' COMMENT '状态：CREATED-已创建，LIVING-直播中，ENDED-已结束',
    max_users INT DEFAULT 1000 COMMENT '最大用户数',
    current_users INT DEFAULT 0 COMMENT '当前用户数',
    scheduled_time DATETIME COMMENT '预定开播时间',
    start_time DATETIME COMMENT '实际开播时间',
    end_time DATETIME COMMENT '结束时间',
    is_recorded TINYINT DEFAULT 0 COMMENT '是否录制',
    record_url VARCHAR(500) COMMENT '录制地址',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    INDEX idx_scheduled_time (scheduled_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='直播间表';

-- 直播会话表
CREATE TABLE IF NOT EXISTS live_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id BIGINT NOT NULL COMMENT '直播间ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    leave_time DATETIME COMMENT '离开时间',
    duration INT DEFAULT 0 COMMENT '观看时长（秒）',
    device_type VARCHAR(20) COMMENT '设备类型',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    FOREIGN KEY (room_id) REFERENCES live_rooms(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_room_id (room_id),
    INDEX idx_user_id (user_id),
    INDEX idx_join_time (join_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='直播会话表';

-- ================================================
-- 分析服务相关表
-- ================================================

-- 课程分析表
CREATE TABLE IF NOT EXISTS course_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL COMMENT '课程ID',
    date DATE NOT NULL COMMENT '统计日期',
    views INT DEFAULT 0 COMMENT '浏览量',
    enrollments INT DEFAULT 0 COMMENT '报名数',
    completions INT DEFAULT 0 COMMENT '完成数',
    avg_progress DECIMAL(5,2) DEFAULT 0 COMMENT '平均进度',
    avg_rating DECIMAL(3,2) DEFAULT 0 COMMENT '平均评分',
    revenue DECIMAL(10,2) DEFAULT 0 COMMENT '收入',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_course_date (course_id, date),
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程分析表';

-- 学习分析表
CREATE TABLE IF NOT EXISTS learning_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    date DATE NOT NULL COMMENT '统计日期',
    study_time INT DEFAULT 0 COMMENT '学习时长（分钟）',
    completed_lessons INT DEFAULT 0 COMMENT '完成课时数',
    completed_courses INT DEFAULT 0 COMMENT '完成课程数',
    quiz_count INT DEFAULT 0 COMMENT '测验次数',
    quiz_avg_score DECIMAL(5,2) DEFAULT 0 COMMENT '测验平均分',
    notes_count INT DEFAULT 0 COMMENT '笔记数量',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_date (user_id, date),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习分析表';

-- ================================================
-- AI服务相关表
-- ================================================

-- 用户行为表
CREATE TABLE IF NOT EXISTS user_behaviors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    behavior_type VARCHAR(50) NOT NULL COMMENT '行为类型',
    target_type VARCHAR(50) COMMENT '目标类型',
    target_id BIGINT COMMENT '目标ID',
    detail JSON COMMENT '详细信息',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_behavior_type (behavior_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户行为表';

-- 推荐结果表
CREATE TABLE IF NOT EXISTS recommendation_results (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type VARCHAR(50) NOT NULL COMMENT '推荐类型',
    items JSON NOT NULL COMMENT '推荐项目',
    algorithm VARCHAR(50) COMMENT '算法名称',
    score DECIMAL(5,2) COMMENT '推荐分数',
    is_clicked TINYINT DEFAULT 0 COMMENT '是否点击',
    is_completed TINYINT DEFAULT 0 COMMENT '是否完成',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expired_at DATETIME COMMENT '过期时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐结果表';

-- ================================================
-- 通用表
-- ================================================

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(20) DEFAULT 'STRING' COMMENT '配置类型',
    description VARCHAR(500) COMMENT '描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    module VARCHAR(50) COMMENT '模块',
    action VARCHAR(50) COMMENT '操作',
    method VARCHAR(200) COMMENT '方法',
    params TEXT COMMENT '参数',
    result TEXT COMMENT '结果',
    ip VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    duration BIGINT COMMENT '耗时（毫秒）',
    status TINYINT COMMENT '状态：0-失败，1-成功',
    error_msg TEXT COMMENT '错误信息',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_module (module),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';