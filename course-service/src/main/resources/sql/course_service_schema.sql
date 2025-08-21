-- 课程服务数据库表结构

USE smart_learning;

-- 课程分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description VARCHAR(500) COMMENT '分类描述',
    icon VARCHAR(255) COMMENT '分类图标',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    level TINYINT DEFAULT 1 COMMENT '分类层级',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    status TINYINT DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    course_count INT DEFAULT 0 COMMENT '课程数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',
    
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level),
    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程分类表';

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '课程ID',
    title VARCHAR(200) NOT NULL COMMENT '课程标题',
    subtitle VARCHAR(300) COMMENT '课程副标题',
    description TEXT COMMENT '课程描述',
    cover_image VARCHAR(500) COMMENT '课程封面图片URL',
    intro_video VARCHAR(500) COMMENT '课程介绍视频URL',
    category_id BIGINT NOT NULL COMMENT '课程分类ID',
    category_name VARCHAR(100) COMMENT '课程分类名称',
    teacher_id BIGINT NOT NULL COMMENT '授课教师ID',
    teacher_name VARCHAR(100) COMMENT '授课教师姓名',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '课程价格',
    original_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '原价',
    difficulty ENUM('BEGINNER','INTERMEDIATE','ADVANCED') DEFAULT 'BEGINNER' COMMENT '课程难度',
    tags VARCHAR(500) COMMENT '课程标签(逗号分隔)',
    status ENUM('DRAFT','REVIEWING','PUBLISHED','OFFLINE') DEFAULT 'DRAFT' COMMENT '课程状态',
    is_free TINYINT DEFAULT 0 COMMENT '是否免费 0:收费 1:免费',
    duration INT DEFAULT 0 COMMENT '课程总时长(分钟)',
    lesson_count INT DEFAULT 0 COMMENT '课时数量',
    student_count INT DEFAULT 0 COMMENT '学习人数',
    favorite_count INT DEFAULT 0 COMMENT '收藏数量',
    rating DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分(1-5分)',
    review_count INT DEFAULT 0 COMMENT '评价数量',
    objectives TEXT COMMENT '课程目标',
    target_audience VARCHAR(1000) COMMENT '适合人群',
    requirements VARCHAR(1000) COMMENT '课程要求',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    is_recommended TINYINT DEFAULT 0 COMMENT '是否推荐 0:否 1:是',
    publish_time DATETIME COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',
    
    INDEX idx_category_id (category_id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    INDEX idx_difficulty (difficulty),
    INDEX idx_is_free (is_free),
    INDEX idx_is_recommended (is_recommended),
    INDEX idx_student_count (student_count),
    INDEX idx_rating (rating),
    INDEX idx_publish_time (publish_time),
    INDEX idx_create_time (create_time),
    INDEX idx_deleted (deleted),
    
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- 章节表
CREATE TABLE IF NOT EXISTS chapters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '章节ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    title VARCHAR(200) NOT NULL COMMENT '章节标题',
    description TEXT COMMENT '章节描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    lesson_count INT DEFAULT 0 COMMENT '课时数量',
    duration INT DEFAULT 0 COMMENT '章节总时长(分钟)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',
    
    INDEX idx_course_id (course_id),
    INDEX idx_sort_order (sort_order),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted),
    
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节表';

-- 课时表
CREATE TABLE IF NOT EXISTS lessons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '课时ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    chapter_id BIGINT NOT NULL COMMENT '章节ID',
    title VARCHAR(200) NOT NULL COMMENT '课时标题',
    description TEXT COMMENT '课时描述',
    type ENUM('VIDEO','DOCUMENT','QUIZ','LIVE') DEFAULT 'VIDEO' COMMENT '课时类型',
    video_url VARCHAR(500) COMMENT '视频URL',
    duration INT DEFAULT 0 COMMENT '视频时长(秒)',
    document_url VARCHAR(500) COMMENT '文档URL',
    content LONGTEXT COMMENT '课时内容',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    is_free TINYINT DEFAULT 0 COMMENT '是否免费试看 0:否 1:是',
    status TINYINT DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
    student_count INT DEFAULT 0 COMMENT '学习人数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识 0:未删除 1:已删除',
    
    INDEX idx_course_id (course_id),
    INDEX idx_chapter_id (chapter_id),
    INDEX idx_type (type),
    INDEX idx_sort_order (sort_order),
    INDEX idx_is_free (is_free),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted),
    
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (chapter_id) REFERENCES chapters(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课时表';

-- 插入默认分类数据
INSERT INTO categories (name, description, icon, parent_id, level, sort_order, status) VALUES
('编程开发', '编程开发相关课程', 'icon-code', 0, 1, 1, 1),
('前端开发', '前端开发技术', 'icon-frontend', 1, 2, 1, 1),
('后端开发', '后端开发技术', 'icon-backend', 1, 2, 2, 1),
('移动开发', '移动应用开发', 'icon-mobile', 1, 2, 3, 1),
('数据科学', '数据科学与分析', 'icon-data', 0, 1, 2, 1),
('人工智能', '人工智能与机器学习', 'icon-ai', 0, 1, 3, 1),
('设计创意', '设计与创意相关', 'icon-design', 0, 1, 4, 1),
('商业管理', '商业与管理', 'icon-business', 0, 1, 5, 1)
ON DUPLICATE KEY UPDATE name = name;

-- 插入示例课程数据
INSERT INTO courses (title, subtitle, description, category_id, category_name, teacher_id, teacher_name, price, original_price, difficulty, tags, status, objectives, target_audience, requirements) VALUES
('Java Spring Boot 微服务开发', '从零开始学习Spring Boot微服务架构', '本课程将带你深入学习Spring Boot微服务开发，包括服务注册发现、配置中心、网关等核心技术。', 3, '后端开发', 1, 'teacher1', 299.00, 399.00, 'INTERMEDIATE', 'Java,Spring Boot,微服务', 'PUBLISHED', '掌握Spring Boot微服务开发技能', '有Java基础的开发者', '熟悉Java基础语法'),
('React 前端开发实战', '现代化前端开发技术栈', '学习React生态系统，包括React Hooks、Redux、React Router等现代前端开发技术。', 2, '前端开发', 1, 'teacher1', 199.00, 299.00, 'BEGINNER', 'React,JavaScript,前端', 'PUBLISHED', '掌握React前端开发技能', '前端开发初学者', '熟悉HTML、CSS、JavaScript基础'),
('Python 数据分析入门', '数据科学基础课程', '使用Python进行数据分析，学习pandas、numpy、matplotlib等数据科学工具库。', 5, '数据科学', 1, 'teacher1', 159.00, 199.00, 'BEGINNER', 'Python,数据分析,pandas', 'PUBLISHED', '掌握Python数据分析基础', '数据分析初学者', '熟悉Python基础语法')
ON DUPLICATE KEY UPDATE title = title;
