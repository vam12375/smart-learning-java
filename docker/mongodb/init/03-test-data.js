// MongoDB 测试数据脚本
db = db.getSiblingDB('smart_learning');

// 插入更多学习笔记测试数据
db.learning_notes.insertMany([
    {
        "_id": ObjectId(),
        "userId": 3,
        "courseId": 3,
        "lessonId": 10,
        "title": "React Hooks使用心得",
        "content": "React Hooks是React 16.8引入的新特性，让我们可以在函数组件中使用state和其他React特性。\n\n常用Hooks：\n1. useState - 状态管理\n2. useEffect - 副作用处理\n3. useContext - 上下文\n4. useReducer - 复杂状态管理\n5. useMemo - 性能优化\n6. useCallback - 回调优化",
        "timePoint": 600,
        "type": "TEXT",
        "isPublic": true,
        "tags": ["React", "Hooks", "前端"],
        "likeCount": 32,
        "createTime": new Date("2024-01-18T16:45:00Z"),
        "updateTime": new Date("2024-01-18T16:45:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "userId": 4,
        "courseId": 4,
        "lessonId": 15,
        "title": "MySQL索引优化策略",
        "content": "数据库索引是提高查询性能的关键技术。\n\n索引优化原则：\n1. 选择合适的索引类型\n2. 避免过多索引\n3. 考虑复合索引\n4. 定期维护索引\n\n常见索引类型：\n- B-Tree索引（默认）\n- Hash索引\n- 全文索引\n- 空间索引",
        "timePoint": 420,
        "type": "TEXT",
        "isPublic": true,
        "tags": ["MySQL", "索引", "数据库", "性能优化"],
        "likeCount": 18,
        "createTime": new Date("2024-01-19T11:20:00Z"),
        "updateTime": new Date("2024-01-19T11:20:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "userId": 5,
        "courseId": 5,
        "lessonId": 20,
        "title": "机器学习算法选择指南",
        "content": "选择合适的机器学习算法是项目成功的关键。\n\n算法选择考虑因素：\n1. 数据量大小\n2. 数据类型（结构化/非结构化）\n3. 问题类型（分类/回归/聚类）\n4. 准确性要求\n5. 训练时间\n6. 可解释性\n\n常用算法：\n- 线性回归：简单快速\n- 决策树：可解释性强\n- 随机森林：准确性高\n- SVM：适合小数据集\n- 神经网络：处理复杂模式",
        "timePoint": 780,
        "type": "TEXT",
        "isPublic": false,
        "tags": ["机器学习", "算法", "人工智能"],
        "likeCount": 45,
        "createTime": new Date("2024-01-20T14:30:00Z"),
        "updateTime": new Date("2024-01-20T14:30:00Z"),
        "deleted": false
    }
]);

// 插入更多学习行为数据
db.learning_behaviors.insertMany([
    {
        "_id": ObjectId(),
        "userId": 3,
        "courseId": 3,
        "chapterId": 5,
        "actionType": 1, // 观看视频
        "duration": 2400, // 40分钟
        "progress": 95.0,
        "deviceType": 2, // 手机
        "browser": "Chrome Mobile",
        "os": "Android 13",
        "ipAddress": "192.168.1.103",
        "location": "广州市",
        "actionTime": new Date("2024-01-18T16:00:00Z"),
        "createTime": new Date("2024-01-18T16:40:00Z")
    },
    {
        "_id": ObjectId(),
        "userId": 4,
        "courseId": 4,
        "chapterId": 8,
        "actionType": 2, // 做练习
        "duration": 1200, // 20分钟
        "progress": 75.0,
        "score": 5,
        "deviceType": 1, // PC
        "browser": "Edge",
        "os": "Windows 11",
        "ipAddress": "192.168.1.104",
        "location": "深圳市",
        "actionTime": new Date("2024-01-19T10:30:00Z"),
        "createTime": new Date("2024-01-19T10:50:00Z")
    },
    {
        "_id": ObjectId(),
        "userId": 5,
        "courseId": 5,
        "chapterId": 12,
        "actionType": 4, // 下载资料
        "duration": 60, // 1分钟
        "progress": 100.0,
        "deviceType": 3, // 平板
        "browser": "Safari",
        "os": "iPadOS 17",
        "ipAddress": "192.168.1.105",
        "location": "杭州市",
        "actionTime": new Date("2024-01-20T13:45:00Z"),
        "createTime": new Date("2024-01-20T13:46:00Z")
    },
    {
        "_id": ObjectId(),
        "userId": 1,
        "courseId": 2,
        "chapterId": 4,
        "actionType": 1, // 观看视频
        "duration": 3600, // 60分钟
        "progress": 100.0,
        "deviceType": 1, // PC
        "browser": "Chrome",
        "os": "Windows 11",
        "ipAddress": "192.168.1.100",
        "location": "北京市",
        "actionTime": new Date("2024-01-21T09:00:00Z"),
        "createTime": new Date("2024-01-21T10:00:00Z")
    }
]);

// 插入更多用户学习统计数据
db.user_learning_stats.insertMany([
    {
        "_id": ObjectId(),
        "userId": 3,
        "statDate": new Date("2024-01-18"),
        "learningDays": 1,
        "totalLearningTime": 150, // 2.5小时
        "coursesLearned": 1,
        "coursesCompleted": 0,
        "videosWatched": 4,
        "exercisesCompleted": 1,
        "discussionsParticipated": 2,
        "materialsDownloaded": 0,
        "avgDailyLearningTime": 150.0,
        "consecutiveDays": 3,
        "maxConsecutiveDays": 5,
        "weeklyGoalProgress": 30.0, // 150/500*100
        "monthlyGoalProgress": 7.5, // 150/2000*100
        "createTime": new Date("2024-01-18T23:59:59Z"),
        "updateTime": new Date("2024-01-18T23:59:59Z")
    },
    {
        "_id": ObjectId(),
        "userId": 4,
        "statDate": new Date("2024-01-19"),
        "learningDays": 1,
        "totalLearningTime": 180, // 3小时
        "coursesLearned": 2,
        "coursesCompleted": 1,
        "videosWatched": 5,
        "exercisesCompleted": 4,
        "discussionsParticipated": 1,
        "materialsDownloaded": 3,
        "avgDailyLearningTime": 180.0,
        "consecutiveDays": 2,
        "maxConsecutiveDays": 7,
        "weeklyGoalProgress": 36.0, // 180/500*100
        "monthlyGoalProgress": 9.0, // 180/2000*100
        "createTime": new Date("2024-01-19T23:59:59Z"),
        "updateTime": new Date("2024-01-19T23:59:59Z")
    },
    {
        "_id": ObjectId(),
        "userId": 5,
        "statDate": new Date("2024-01-20"),
        "learningDays": 1,
        "totalLearningTime": 240, // 4小时
        "coursesLearned": 1,
        "coursesCompleted": 0,
        "videosWatched": 6,
        "exercisesCompleted": 2,
        "discussionsParticipated": 3,
        "materialsDownloaded": 5,
        "avgDailyLearningTime": 240.0,
        "consecutiveDays": 1,
        "maxConsecutiveDays": 10,
        "weeklyGoalProgress": 48.0, // 240/500*100
        "monthlyGoalProgress": 12.0, // 240/2000*100
        "createTime": new Date("2024-01-20T23:59:59Z"),
        "updateTime": new Date("2024-01-20T23:59:59Z")
    }
]);

// 插入课程评论数据
db.course_comments.insertMany([
    {
        "_id": ObjectId(),
        "courseId": 2,
        "userId": 3,
        "username": "前端开发者",
        "avatar": "https://example.com/avatar3.jpg",
        "content": "Spring Boot课程内容很全面，从基础到进阶都有涉及，特别是微服务部分讲得很详细。",
        "rating": 5,
        "likeCount": 20,
        "replyCount": 5,
        "isTop": true,
        "createTime": new Date("2024-01-18T20:30:00Z"),
        "updateTime": new Date("2024-01-18T20:30:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "courseId": 3,
        "userId": 4,
        "username": "React学习者",
        "avatar": "https://example.com/avatar4.jpg",
        "content": "React课程很实用，老师讲解清晰，项目案例也很贴近实际开发。建议增加更多Hooks的实战案例。",
        "rating": 4,
        "likeCount": 15,
        "replyCount": 2,
        "isTop": false,
        "createTime": new Date("2024-01-19T15:20:00Z"),
        "updateTime": new Date("2024-01-19T15:20:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "courseId": 4,
        "userId": 5,
        "username": "数据库专家",
        "avatar": "https://example.com/avatar5.jpg",
        "content": "MySQL课程内容深入浅出，从基础语法到性能优化都有详细讲解，对工作帮助很大！",
        "rating": 5,
        "likeCount": 25,
        "replyCount": 8,
        "isTop": false,
        "createTime": new Date("2024-01-20T11:45:00Z"),
        "updateTime": new Date("2024-01-20T11:45:00Z"),
        "deleted": false
    }
]);

// 插入聊天消息数据
db.chat_messages.insertMany([
    {
        "_id": ObjectId(),
        "roomId": 1,
        "userId": 1,
        "username": "学习者小王",
        "avatar": "https://example.com/avatar1.jpg",
        "content": "大家好，我是新来的，请多多指教！",
        "messageType": 1, // 普通消息
        "muted": false,
        "createTime": new Date("2024-01-21T10:00:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "roomId": 1,
        "userId": 2,
        "username": "编程爱好者",
        "avatar": "https://example.com/avatar2.jpg",
        "content": "欢迎新同学！有问题可以随时提问。",
        "messageType": 1, // 普通消息
        "muted": false,
        "createTime": new Date("2024-01-21T10:01:00Z"),
        "deleted": false
    },
    {
        "_id": ObjectId(),
        "roomId": 1,
        "userId": 0,
        "username": "系统",
        "avatar": "",
        "content": "用户 '学习者小王' 加入了直播间",
        "messageType": 2, // 系统消息
        "muted": false,
        "createTime": new Date("2024-01-21T09:59:30Z"),
        "deleted": false
    }
]);

// 插入推荐记录数据
db.recommendations.insertMany([
    {
        "_id": ObjectId(),
        "userId": 1,
        "algorithmType": "collaborative_filtering",
        "recommendedItems": [
            {
                "itemId": 3,
                "itemType": "course",
                "title": "React前端开发实战",
                "score": 0.85,
                "reason": "基于相似用户的学习偏好"
            },
            {
                "itemId": 4,
                "itemType": "course", 
                "title": "MySQL数据库进阶",
                "score": 0.78,
                "reason": "与您已学课程相关"
            }
        ],
        "createTime": new Date("2024-01-21T08:00:00Z"),
        "expireTime": new Date("2024-01-28T08:00:00Z"),
        "clicked": false,
        "applied": false
    },
    {
        "_id": ObjectId(),
        "userId": 2,
        "algorithmType": "content_based",
        "recommendedItems": [
            {
                "itemId": 5,
                "itemType": "course",
                "title": "机器学习入门",
                "score": 0.92,
                "reason": "基于您的学习历史和兴趣"
            },
            {
                "itemId": 2,
                "itemType": "course",
                "title": "Spring Boot微服务",
                "score": 0.76,
                "reason": "热门推荐"
            }
        ],
        "createTime": new Date("2024-01-21T08:30:00Z"),
        "expireTime": new Date("2024-01-28T08:30:00Z"),
        "clicked": true,
        "applied": false
    }
]);

print("Test data inserted successfully!");
