-- ================================================
-- 智能学习平台示例数据
-- Database: smart_learning
-- Version: 1.0.0
-- Date: 2025-08-29
-- ================================================

USE smart_learning;

-- ================================================
-- 用户数据
-- ================================================

-- 插入管理员
INSERT INTO users (username, password, email, phone, nickname, avatar, gender, role, status) VALUES
('admin', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'admin@smartlearning.com', '13800138000', '系统管理员', '/avatars/admin.jpg', 1, 'ADMIN', 1);

-- 插入教师用户
INSERT INTO users (username, password, email, phone, nickname, avatar, gender, role, status) VALUES
('teacher_zhang', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'zhang@smartlearning.com', '13900139001', '张老师', '/avatars/teacher1.jpg', 1, 'TEACHER', 1),
('teacher_li', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'li@smartlearning.com', '13900139002', '李老师', '/avatars/teacher2.jpg', 2, 'TEACHER', 1),
('teacher_wang', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'wang@smartlearning.com', '13900139003', '王老师', '/avatars/teacher3.jpg', 1, 'TEACHER', 1),
('teacher_chen', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'chen@smartlearning.com', '13900139004', '陈老师', '/avatars/teacher4.jpg', 2, 'TEACHER', 1);

-- 插入学生用户
INSERT INTO users (username, password, email, phone, nickname, avatar, gender, role, status, birthday) VALUES
('student1', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'student1@example.com', '13800138001', '小明', '/avatars/student1.jpg', 1, 'STUDENT', 1, '2000-05-15'),
('student2', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'student2@example.com', '13800138002', '小红', '/avatars/student2.jpg', 2, 'STUDENT', 1, '2001-03-20'),
('student3', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'student3@example.com', '13800138003', '小刚', '/avatars/student3.jpg', 1, 'STUDENT', 1, '1999-11-08'),
('student4', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'student4@example.com', '13800138004', '小丽', '/avatars/student4.jpg', 2, 'STUDENT', 1, '2002-07-25'),
('student5', '$2a$10$EixZaYVK1fsbw1Zfb6L1.edOxRIB4rN3xgqS6/86LweaNutGnAS5K', 'student5@example.com', '13800138005', '小强', '/avatars/student5.jpg', 1, 'STUDENT', 1, '2000-12-10');

-- 插入用户详细信息
INSERT INTO user_profiles (user_id, real_name, education, school, major, bio, interests, city) VALUES
(2, '张建国', '博士', '清华大学', '计算机科学与技术', '10年软件开发经验，专注于前端技术和架构设计', '编程、阅读、摄影', '北京'),
(3, '李美华', '硕士', '北京大学', '软件工程', '资深后端工程师，精通微服务架构和分布式系统', '技术写作、开源项目', '上海'),
(4, '王大伟', '硕士', '复旦大学', '数据科学', '数据科学专家，专注于机器学习和数据分析', 'AI、数据挖掘、算法', '深圳'),
(5, '陈小芳', '硕士', '浙江大学', '信息安全', '网络安全专家，擅长渗透测试和安全架构', '安全研究、CTF', '杭州');

-- 插入用户统计数据
INSERT INTO user_stats (user_id, total_study_time, total_courses, completed_courses, total_exams, passed_exams, total_credits, points) VALUES
(6, 1250, 5, 2, 8, 6, 15, 580),
(7, 890, 3, 1, 5, 4, 8, 320),
(8, 2100, 8, 5, 12, 10, 25, 950),
(9, 560, 2, 0, 3, 2, 5, 180),
(10, 1680, 6, 3, 9, 7, 18, 720);

-- ================================================
-- 课程分类数据
-- ================================================

INSERT INTO course_categories (name, description, parent_id, icon, sort_order, status) VALUES
('前端开发', '学习HTML、CSS、JavaScript等前端技术', 0, 'desktop', 1, 1),
('后端开发', '学习Java、Python、Go等后端技术', 0, 'code', 2, 1),
('移动开发', '学习Android、iOS、Flutter等移动技术', 0, 'mobile', 3, 1),
('数据库', '学习MySQL、MongoDB、Redis等数据库技术', 0, 'database', 4, 1),
('云计算', '学习AWS、阿里云、Docker等云技术', 0, 'cloud', 5, 1),
('人工智能', '学习机器学习、深度学习、NLP等AI技术', 0, 'robot', 6, 1),
('网络安全', '学习网络安全、渗透测试、加密技术', 0, 'safety', 7, 1),
('数据分析', '学习数据分析、数据可视化、商业智能', 0, 'line-chart', 8, 1);

-- 插入子分类
INSERT INTO course_categories (name, description, parent_id, icon, sort_order, status) VALUES
('Vue.js', 'Vue.js框架开发', 1, 'vuejs', 1, 1),
('React', 'React框架开发', 1, 'react', 2, 1),
('Angular', 'Angular框架开发', 1, 'angular', 3, 1),
('Spring Boot', 'Spring Boot框架开发', 2, 'spring', 1, 1),
('Django', 'Django框架开发', 2, 'python', 2, 1),
('机器学习', '机器学习算法和应用', 6, 'robot', 1, 1),
('深度学习', '深度学习和神经网络', 6, 'brain', 2, 1);

-- ================================================
-- 课程数据
-- ================================================

INSERT INTO courses (title, subtitle, description, cover_image, category_id, teacher_id, price, original_price, difficulty, duration, lesson_count, student_count, rating, rating_count, status, is_free, is_featured, tags, requirements, objectives) VALUES
-- Vue.js课程
('Vue.js 3.0 完整教程', '从零开始学习Vue.js 3.0', 
'本课程涵盖Vue 3的所有核心概念，包括Composition API、响应式系统、组件开发、路由管理、状态管理等。通过实战项目帮助你掌握Vue.js开发技能。', 
'/images/vue-course.jpg', 9, 2, 199.00, 299.00, 'INTERMEDIATE', 720, 48, 1580, 4.8, 320, 'PUBLISHED', 0, 1,
'Vue.js,前端,JavaScript,TypeScript',
'基础的HTML、CSS和JavaScript知识;了解ES6+语法;有编程基础',
'掌握Vue.js 3.0核心概念;学会使用Composition API;能够独立开发Vue应用;理解响应式原理'),

-- Spring Boot课程
('Spring Boot企业级开发', '构建高性能微服务架构', 
'深入学习Spring Boot核心技术，掌握企业级应用开发技能。课程包括Spring Boot基础、数据访问、安全认证、微服务架构、容器化部署等内容。', 
'/images/spring-course.jpg', 12, 3, 299.00, 399.00, 'ADVANCED', 1080, 60, 2100, 4.9, 450, 'PUBLISHED', 0, 1,
'Spring Boot,Java,微服务,Docker',
'Java基础知识;了解Spring框架;熟悉Maven或Gradle',
'掌握Spring Boot开发;学会微服务架构设计;能够构建企业级应用;掌握容器化部署'),

-- Python数据分析课程
('Python数据分析入门', '零基础学习数据科学', 
'使用Python进行数据分析，掌握NumPy、Pandas、Matplotlib等核心库。学习数据清洗、数据可视化、统计分析等技能。', 
'/images/python-course.jpg', 8, 4, 0, 0, 'BEGINNER', 540, 36, 3200, 4.7, 680, 'PUBLISHED', 1, 0,
'Python,数据分析,Pandas,NumPy',
'无需编程基础;对数据分析感兴趣',
'掌握Python基础语法;学会使用数据分析库;能够进行数据清洗和可视化;理解基本统计概念'),

-- React课程
('React实战开发', '打造现代化Web应用', 
'全面学习React生态系统，包括React Hooks、Redux、React Router等。通过实战项目提升React开发能力。', 
'/images/react-course.jpg', 10, 2, 249.00, 349.00, 'INTERMEDIATE', 900, 52, 890, 4.6, 180, 'PUBLISHED', 0, 0,
'React,前端,JavaScript,Redux',
'JavaScript基础;了解ES6语法;有HTML/CSS经验',
'掌握React核心概念;学会使用Hooks;能够管理应用状态;掌握React生态系统'),

-- 机器学习课程
('机器学习实战', '从理论到实践的完整指南', 
'系统学习机器学习算法，包括监督学习、无监督学习、深度学习基础。使用Python和TensorFlow实现各种算法。', 
'/images/ml-course.jpg', 14, 4, 399.00, 599.00, 'ADVANCED', 1440, 72, 560, 4.9, 120, 'PUBLISHED', 0, 1,
'机器学习,Python,TensorFlow,深度学习',
'Python编程基础;高等数学知识;线性代数基础',
'理解机器学习原理;掌握常用算法;能够处理实际问题;学会模型评估和优化'),

-- Docker课程
('Docker容器化技术', '微服务部署必备技能', 
'学习Docker基础知识、镜像构建、容器编排、Docker Compose、Kubernetes入门等内容。', 
'/images/docker-course.jpg', 5, 3, 159.00, 259.00, 'INTERMEDIATE', 480, 32, 1250, 4.7, 280, 'PUBLISHED', 0, 0,
'Docker,容器化,DevOps,Kubernetes',
'Linux基础知识;了解虚拟化概念',
'掌握Docker使用;学会构建镜像;能够编排容器;了解Kubernetes基础');

-- ================================================
-- 章节数据
-- ================================================

-- Vue.js课程章节
INSERT INTO course_chapters (course_id, title, description, sort_order, duration, lesson_count) VALUES
(1, '课程介绍与环境搭建', 'Vue.js简介、开发环境配置、第一个Vue应用', 1, 60, 4),
(1, 'Vue基础语法', '模板语法、数据绑定、条件渲染、列表渲染', 2, 120, 8),
(1, '组件开发', '组件基础、Props、自定义事件、插槽', 3, 180, 12),
(1, 'Composition API', 'setup函数、响应式API、生命周期钩子', 4, 150, 10),
(1, 'Vue Router路由', '路由配置、导航守卫、动态路由', 5, 120, 8),
(1, 'Vuex状态管理', 'State、Mutations、Actions、Modules', 6, 90, 6);

-- Spring Boot课程章节
INSERT INTO course_chapters (course_id, title, description, sort_order, duration, lesson_count) VALUES
(2, 'Spring Boot基础', 'Spring Boot简介、自动配置、起步依赖', 1, 90, 6),
(2, '数据访问层', 'JPA、MyBatis、事务管理', 2, 150, 10),
(2, 'Web开发', 'RESTful API、参数校验、异常处理', 3, 180, 12),
(2, '安全认证', 'Spring Security、JWT、OAuth2', 4, 210, 14),
(2, '微服务架构', '服务注册发现、配置中心、网关', 5, 240, 12),
(2, '部署与监控', 'Docker部署、健康检查、日志管理', 6, 210, 6);

-- ================================================
-- 课时数据（部分示例）
-- ================================================

-- Vue.js第一章课时
INSERT INTO course_lessons (chapter_id, course_id, title, description, type, video_url, video_duration, sort_order, is_free, status) VALUES
(1, 1, '课程导学', '了解课程安排和学习方法', 'VIDEO', '/videos/vue/1-1.mp4', 600, 1, 1, 1),
(1, 1, 'Vue.js是什么', 'Vue.js框架介绍和特点', 'VIDEO', '/videos/vue/1-2.mp4', 900, 2, 1, 1),
(1, 1, '开发环境搭建', '安装Node.js、Vue CLI', 'VIDEO', '/videos/vue/1-3.mp4', 1200, 3, 0, 1),
(1, 1, '创建第一个Vue应用', '使用Vue CLI创建项目', 'VIDEO', '/videos/vue/1-4.mp4', 1500, 4, 0, 1);

-- Vue.js第二章课时
INSERT INTO course_lessons (chapter_id, course_id, title, description, type, video_url, video_duration, sort_order, is_free, status) VALUES
(2, 1, '模板语法基础', '插值、指令、缩写', 'VIDEO', '/videos/vue/2-1.mp4', 1200, 1, 0, 1),
(2, 1, '数据绑定', 'v-model双向绑定', 'VIDEO', '/videos/vue/2-2.mp4', 900, 2, 0, 1),
(2, 1, '条件渲染', 'v-if、v-show使用', 'VIDEO', '/videos/vue/2-3.mp4', 800, 3, 0, 1),
(2, 1, '列表渲染', 'v-for循环渲染', 'VIDEO', '/videos/vue/2-4.mp4', 1000, 4, 0, 1);

-- ================================================
-- 学习进度数据
-- ================================================

INSERT INTO learning_progress (user_id, course_id, lesson_id, total_lessons, completed_lessons, progress, total_duration, last_study_time, status) VALUES
(6, 1, 4, 48, 12, 25.00, 7200, '2025-08-28 15:30:00', 'LEARNING'),
(6, 2, NULL, 60, 0, 0.00, 0, NULL, 'LEARNING'),
(7, 1, 8, 48, 8, 16.67, 4800, '2025-08-27 20:15:00', 'LEARNING'),
(8, 1, 48, 48, 48, 100.00, 43200, '2025-08-25 18:45:00', 'COMPLETED'),
(8, 3, 20, 36, 20, 55.56, 10800, '2025-08-28 19:20:00', 'LEARNING');

-- ================================================
-- 学习记录数据
-- ================================================

INSERT INTO learning_records (user_id, course_id, lesson_id, start_time, end_time, duration, progress, is_completed) VALUES
(6, 1, 1, '2025-08-26 09:00:00', '2025-08-26 09:10:00', 600, 600, 1),
(6, 1, 2, '2025-08-26 09:15:00', '2025-08-26 09:30:00', 900, 900, 1),
(6, 1, 3, '2025-08-26 09:35:00', '2025-08-26 09:55:00', 1200, 1200, 1),
(6, 1, 4, '2025-08-26 10:00:00', '2025-08-26 10:25:00', 1500, 1500, 1),
(7, 1, 1, '2025-08-27 14:00:00', '2025-08-27 14:10:00', 600, 600, 1);

-- ================================================
-- 学习笔记数据
-- ================================================

INSERT INTO learning_notes (user_id, course_id, lesson_id, title, content, video_time, is_public, likes) VALUES
(6, 1, 1, 'Vue.js学习要点', 'Vue.js是一个渐进式JavaScript框架，核心库只关注视图层。主要特点：1.响应式数据绑定 2.组件化开发 3.虚拟DOM', 180, 1, 5),
(6, 1, 3, '环境配置注意事项', 'Node.js版本建议使用LTS版本，npm可以换成yarn或pnpm提高安装速度。Vue CLI可以全局安装：npm install -g @vue/cli', 450, 1, 3),
(8, 1, 10, '组件通信方式总结', '1.Props父传子 2.Emit子传父 3.Provide/Inject跨级传递 4.Vuex全局状态 5.EventBus事件总线', NULL, 1, 12);

-- ================================================
-- 课程收藏数据
-- ================================================

INSERT INTO course_favorites (user_id, course_id) VALUES
(6, 1), (6, 2), (6, 5),
(7, 1), (7, 3),
(8, 1), (8, 2), (8, 3), (8, 4),
(9, 3), (9, 6),
(10, 1), (10, 2), (10, 4), (10, 5);

-- ================================================
-- 题库数据
-- ================================================

INSERT INTO questions (course_id, chapter_id, type, difficulty, title, options, answer, analysis, score, tags, status) VALUES
(1, 1, 'SINGLE', 'EASY', 'Vue.js的作者是谁？', 
'["Evan You", "Dan Abramov", "Ryan Dahl", "Brendan Eich"]', 
'A', 
'Vue.js是由Evan You（尤雨溪）创建的开源JavaScript框架。', 
2, 'Vue基础,框架历史', 1),

(1, 2, 'MULTIPLE', 'MEDIUM', 'Vue中的指令有哪些？（多选）', 
'["v-model", "v-for", "v-if", "v-state"]', 
'A,B,C', 
'v-model用于双向绑定，v-for用于列表渲染，v-if用于条件渲染。v-state不是Vue的指令。', 
3, 'Vue指令,模板语法', 1),

(1, 3, 'JUDGE', 'EASY', 'Vue组件的data必须是一个函数。', 
'', 
'true', 
'组件的data必须是函数，这样每个组件实例才能维护一份独立的数据副本。', 
1, 'Vue组件,最佳实践', 1),

(2, 7, 'SINGLE', 'MEDIUM', 'Spring Boot的默认端口是？', 
'["8080", "8081", "3000", "5000"]', 
'A', 
'Spring Boot应用的默认端口是8080，可以通过配置文件修改。', 
2, 'Spring Boot,配置', 1),

(2, 8, 'FILL', 'EASY', 'Spring Boot的核心注解是____。', 
'', 
'@SpringBootApplication', 
'@SpringBootApplication是Spring Boot的核心注解，包含了@Configuration、@EnableAutoConfiguration和@ComponentScan。', 
2, 'Spring Boot,注解', 1);

-- ================================================
-- 试卷数据
-- ================================================

INSERT INTO exam_papers (title, description, course_id, type, total_score, pass_score, time_limit, question_count, status, created_by) VALUES
('Vue.js基础测验', '测试Vue.js基础知识掌握情况', 1, 'QUIZ', 100, 60, 30, 10, 'PUBLISHED', 2),
('Spring Boot期中考试', 'Spring Boot课程期中考试', 2, 'EXAM', 100, 60, 90, 20, 'PUBLISHED', 3),
('Python数据分析练习', 'Python数据分析基础练习', 3, 'PRACTICE', 50, 30, 60, 5, 'PUBLISHED', 4);

-- ================================================
-- 试卷题目关联数据
-- ================================================

INSERT INTO exam_paper_questions (paper_id, question_id, sort_order, score) VALUES
(1, 1, 1, 10),
(1, 2, 2, 15),
(1, 3, 3, 10),
(2, 4, 1, 10),
(2, 5, 2, 10);

-- ================================================
-- 考试记录数据
-- ================================================

INSERT INTO exam_records (user_id, paper_id, start_time, submit_time, time_used, score, status, is_passed, answers) VALUES
(6, 1, '2025-08-25 10:00:00', '2025-08-25 10:25:00', 1500, 75.00, 'MARKED', 1, 
'{"1": "A", "2": ["A", "B", "C"], "3": "true"}'),
(7, 1, '2025-08-26 14:00:00', '2025-08-26 14:28:00', 1680, 55.00, 'MARKED', 0, 
'{"1": "B", "2": ["A", "B"], "3": "true"}');

-- ================================================
-- 直播间数据
-- ================================================

INSERT INTO live_rooms (title, description, cover_image, course_id, teacher_id, room_key, status, max_users, scheduled_time, is_recorded) VALUES
('Vue.js答疑直播', '解答Vue.js学习中的问题', '/images/live1.jpg', 1, 2, 'ROOM_VUE_001', 'CREATED', 500, '2025-08-30 20:00:00', 1),
('Spring Boot实战分享', 'Spring Boot项目实战经验分享', '/images/live2.jpg', 2, 3, 'ROOM_SPRING_001', 'CREATED', 1000, '2025-08-31 19:00:00', 1),
('Python数据分析案例', '真实数据分析案例讲解', '/images/live3.jpg', 3, 4, 'ROOM_PYTHON_001', 'ENDED', 300, '2025-08-25 15:00:00', 1);

-- ================================================
-- 课程分析数据
-- ================================================

INSERT INTO course_analytics (course_id, date, views, enrollments, completions, avg_progress, avg_rating, revenue) VALUES
(1, '2025-08-25', 450, 12, 2, 35.50, 4.8, 2388.00),
(1, '2025-08-26', 380, 8, 1, 36.20, 4.8, 1592.00),
(1, '2025-08-27', 520, 15, 3, 37.80, 4.8, 2985.00),
(1, '2025-08-28', 490, 10, 2, 38.50, 4.8, 1990.00),
(2, '2025-08-25', 320, 5, 0, 22.30, 4.9, 1495.00),
(2, '2025-08-26', 280, 3, 1, 23.50, 4.9, 897.00),
(2, '2025-08-27', 350, 7, 0, 24.10, 4.9, 2093.00),
(3, '2025-08-25', 680, 25, 5, 45.60, 4.7, 0.00),
(3, '2025-08-26', 720, 28, 8, 46.80, 4.7, 0.00);

-- ================================================
-- 学习分析数据
-- ================================================

INSERT INTO learning_analytics (user_id, date, study_time, completed_lessons, completed_courses, quiz_count, quiz_avg_score, notes_count) VALUES
(6, '2025-08-25', 120, 3, 0, 1, 75.00, 2),
(6, '2025-08-26', 150, 4, 0, 0, 0.00, 3),
(6, '2025-08-27', 90, 2, 0, 0, 0.00, 1),
(6, '2025-08-28', 180, 3, 0, 1, 82.00, 2),
(7, '2025-08-26', 60, 1, 0, 1, 55.00, 0),
(7, '2025-08-27', 120, 3, 0, 0, 0.00, 1),
(8, '2025-08-25', 240, 8, 1, 2, 88.50, 5);

-- ================================================
-- 用户行为数据
-- ================================================

INSERT INTO user_behaviors (user_id, behavior_type, target_type, target_id, detail) VALUES
(6, 'VIEW_COURSE', 'COURSE', 1, '{"duration": 30, "source": "home"}'),
(6, 'ENROLL_COURSE', 'COURSE', 1, '{"price": 199.00, "payment": "alipay"}'),
(6, 'WATCH_VIDEO', 'LESSON', 1, '{"duration": 600, "completion": 100}'),
(6, 'LIKE_NOTE', 'NOTE', 1, '{"note_user": 8}'),
(7, 'SEARCH', 'KEYWORD', NULL, '{"keyword": "Vue.js", "results": 15}'),
(7, 'VIEW_COURSE', 'COURSE', 1, '{"duration": 45, "source": "search"}'),
(8, 'COMPLETE_COURSE', 'COURSE', 1, '{"total_time": 43200, "score": 95}');

-- ================================================
-- 推荐结果数据
-- ================================================

INSERT INTO recommendation_results (user_id, type, items, algorithm, score, is_clicked, expired_at) VALUES
(6, 'COURSE', '[2, 4, 5]', 'CF', 0.85, 1, '2025-09-01 00:00:00'),
(7, 'COURSE', '[1, 3, 6]', 'CB', 0.78, 0, '2025-09-01 00:00:00'),
(8, 'COURSE', '[4, 5, 6]', 'HYBRID', 0.92, 1, '2025-09-01 00:00:00');

-- ================================================
-- 系统配置数据
-- ================================================

INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
('site.name', '智能学习平台', 'STRING', '网站名称'),
('site.logo', '/images/logo.png', 'STRING', '网站Logo'),
('site.description', '专业的在线学习平台，提供高质量的编程课程', 'STRING', '网站描述'),
('upload.max_size', '10485760', 'INTEGER', '上传文件最大大小（字节）'),
('video.supported_formats', 'mp4,avi,mov,wmv', 'STRING', '支持的视频格式'),
('course.trial_duration', '300', 'INTEGER', '免费试看时长（秒）'),
('payment.alipay.enabled', 'true', 'BOOLEAN', '是否启用支付宝支付'),
('payment.wechat.enabled', 'true', 'BOOLEAN', '是否启用微信支付'),
('email.smtp.host', 'smtp.example.com', 'STRING', 'SMTP服务器地址'),
('email.smtp.port', '587', 'INTEGER', 'SMTP服务器端口');

-- ================================================
-- 操作日志数据（示例）
-- ================================================

INSERT INTO operation_logs (user_id, username, module, action, method, params, ip, status, duration) VALUES
(6, 'student1', '课程', '报名', 'CourseController.enrollCourse', '{"courseId": 1}', '192.168.1.100', 1, 125),
(6, 'student1', '学习', '观看视频', 'LearningController.watchVideo', '{"lessonId": 1}', '192.168.1.100', 1, 85),
(7, 'student2', '考试', '提交试卷', 'ExamController.submitPaper', '{"paperId": 1}', '192.168.1.101', 1, 230),
(2, 'teacher_zhang', '课程', '创建课程', 'CourseController.createCourse', '{"title": "Vue.js 3.0 完整教程"}', '192.168.1.50', 1, 450),
(1, 'admin', '系统', '修改配置', 'SystemController.updateConfig', '{"key": "site.name"}', '192.168.1.1', 1, 65);

-- ================================================
-- 数据统计更新
-- ================================================

-- 更新课程的学生数和收藏数
UPDATE courses c SET 
    student_count = (SELECT COUNT(*) FROM learning_progress WHERE course_id = c.id),
    favorite_count = (SELECT COUNT(*) FROM course_favorites WHERE course_id = c.id)
WHERE id IN (1, 2, 3, 4, 5, 6);

-- 更新用户统计信息
UPDATE user_stats us SET
    total_courses = (SELECT COUNT(*) FROM learning_progress WHERE user_id = us.user_id),
    completed_courses = (SELECT COUNT(*) FROM learning_progress WHERE user_id = us.user_id AND status = 'COMPLETED')
WHERE user_id IN (6, 7, 8, 9, 10);