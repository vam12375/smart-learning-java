// MongoDB 示例数据插入脚本
db = db.getSiblingDB('smart_learning');

// 插入学习笔记示例数据
db.learning_notes.insertMany([
    {
        "_id": ObjectId(),
        "userId": 1,
        "courseId": 1,
        "lessonId": 1,
        "title": "Java基础语法笔记",
        "content": "Java是一种面向对象的编程语言，具有跨平台、安全性高等特点。\n\n主要特性：\n1. 面向对象\n2. 跨平台性\n3. 安全性\n4. 多线程支持",
        "timePoint": 120,
        "type": "TEXT",
        "isPublic": true,
        "tags": ["Java", "基础", "语法"],
        "likeCount": 15,
        "createTime": new Date("2024-01-15T10:30:00Z"),
        "updateTime": new Date("2024-01-15T10:30:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "userId": 2,
        "courseId": 1,
        "lessonId": 2,
        "title": "变量和数据类型",
        "content": "Java中的基本数据类型包括：\n- byte: 8位\n- short: 16位\n- int: 32位\n- long: 64位\n- float: 32位浮点\n- double: 64位浮点\n- boolean: 布尔值\n- char: 字符",
        "timePoint": 300,
        "type": "TEXT",
        "isPublic": true,
        "tags": ["Java", "数据类型", "变量"],
        "likeCount": 8,
        "createTime": new Date("2024-01-16T14:20:00Z"),
        "updateTime": new Date("2024-01-16T14:20:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "userId": 1,
        "courseId": 2,
        "lessonId": 5,
        "title": "Spring Boot自动配置原理",
        "content": "Spring Boot的自动配置是通过@EnableAutoConfiguration注解实现的。\n\n核心机制：\n1. 条件注解(@Conditional)\n2. 自动配置类\n3. spring.factories文件\n4. 配置属性绑定",
        "timePoint": 450,
        "type": "TEXT",
        "isPublic": false,
        "tags": ["Spring Boot", "自动配置", "原理"],
        "likeCount": 23,
        "createTime": new Date("2024-01-20T09:15:00Z"),
        "updateTime": new Date("2024-01-20T09:15:00Z"),
        "deleted": false
    }
]);

// 插入学习行为分析示例数据
db.learning_behaviors.insertMany([
    {
        "_id": ObjectId(),
        "userId": 1,
        "courseId": 1,
        "chapterId": 1,
        "actionType": 1, // 观看视频
        "duration": 1800, // 30分钟
        "progress": 85.5,
        "deviceType": 1, // PC
        "browser": "Chrome",
        "os": "Windows 11",
        "ipAddress": "192.168.1.100",
        "location": "北京市",
        "actionTime": new Date("2024-01-15T10:00:00Z"),
        "createTime": new Date("2024-01-15T10:30:00Z")
    },
    {
        "_id": ObjectId(),
        "userId": 1,
        "courseId": 1,
        "chapterId": 1,
        "actionType": 3, // 参与讨论
        "duration": 300, // 5分钟
        "progress": 85.5,
        "deviceType": 2, // 手机
        "browser": "Safari",
        "os": "iOS 17",
        "ipAddress": "192.168.1.101",
        "location": "北京市",
        "actionTime": new Date("2024-01-15T15:30:00Z"),
        "createTime": new Date("2024-01-15T15:35:00Z")
    },
    {
        "_id": ObjectId(),
        "userId": 2,
        "courseId": 2,
        "chapterId": 3,
        "actionType": 2, // 做练习
        "duration": 900, // 15分钟
        "progress": 60.0,
        "score": 4,
        "deviceType": 1, // PC
        "browser": "Firefox",
        "os": "macOS",
        "ipAddress": "192.168.1.102",
        "location": "上海市",
        "actionTime": new Date("2024-01-16T14:00:00Z"),
        "createTime": new Date("2024-01-16T14:15:00Z")
    }
]);

// 插入用户学习统计示例数据
db.user_learning_stats.insertMany([
    {
        "_id": ObjectId(),
        "userId": 1,
        "statDate": new Date("2024-01-15"),
        "learningDays": 1,
        "totalLearningTime": 120, // 2小时
        "coursesLearned": 2,
        "coursesCompleted": 0,
        "videosWatched": 3,
        "exercisesCompleted": 2,
        "discussionsParticipated": 1,
        "materialsDownloaded": 1,
        "avgDailyLearningTime": 120.0,
        "consecutiveDays": 1,
        "maxConsecutiveDays": 1,
        "weeklyGoalProgress": 24.0, // 120/500*100
        "monthlyGoalProgress": 6.0, // 120/2000*100
        "createTime": new Date("2024-01-15T23:59:59Z"),
        "updateTime": new Date("2024-01-15T23:59:59Z")
    },
    {
        "_id": ObjectId(),
        "userId": 2,
        "statDate": new Date("2024-01-16"),
        "learningDays": 1,
        "totalLearningTime": 90, // 1.5小时
        "coursesLearned": 1,
        "coursesCompleted": 0,
        "videosWatched": 2,
        "exercisesCompleted": 3,
        "discussionsParticipated": 0,
        "materialsDownloaded": 2,
        "avgDailyLearningTime": 90.0,
        "consecutiveDays": 1,
        "maxConsecutiveDays": 1,
        "weeklyGoalProgress": 18.0, // 90/500*100
        "monthlyGoalProgress": 4.5, // 90/2000*100
        "createTime": new Date("2024-01-16T23:59:59Z"),
        "updateTime": new Date("2024-01-16T23:59:59Z")
    }
]);

// 插入课程评论示例数据
db.course_comments.insertMany([
    {
        "_id": ObjectId(),
        "courseId": 1,
        "userId": 1,
        "username": "学习者小王",
        "avatar": "https://example.com/avatar1.jpg",
        "content": "这门Java课程讲解得非常详细，老师的教学方式很容易理解，推荐给初学者！",
        "rating": 5,
        "likeCount": 12,
        "replyCount": 3,
        "isTop": false,
        "createTime": new Date("2024-01-15T16:30:00Z"),
        "updateTime": new Date("2024-01-15T16:30:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "courseId": 1,
        "userId": 2,
        "username": "编程爱好者",
        "avatar": "https://example.com/avatar2.jpg",
        "content": "课程内容很实用，但是希望能增加更多的实战项目案例。",
        "rating": 4,
        "likeCount": 8,
        "replyCount": 1,
        "isTop": false,
        "createTime": new Date("2024-01-16T10:15:00Z"),
        "updateTime": new Date("2024-01-16T10:15:00Z"),
        "deleted": false
    }
]);

print("Sample data inserted successfully!");
