// MongoDB 初始化脚本
// 切换到 smart_learning 数据库
db = db.getSiblingDB('smart_learning');

// 创建学习笔记集合
db.createCollection('learning_notes');

// 创建学习行为分析集合
db.createCollection('learning_behaviors');

// 创建用户学习统计集合
db.createCollection('user_learning_stats');

// 创建课程评论集合
db.createCollection('course_comments');

// 创建聊天消息集合
db.createCollection('chat_messages');

// 创建推荐记录集合
db.createCollection('recommendations');

// 为学习笔记集合创建索引
db.learning_notes.createIndex({ "userId": 1, "courseId": 1 });
db.learning_notes.createIndex({ "userId": 1, "createTime": -1 });
db.learning_notes.createIndex({ "courseId": 1, "isPublic": 1 });

// 为学习行为分析集合创建索引
db.learning_behaviors.createIndex({ "userId": 1, "actionTime": -1 });
db.learning_behaviors.createIndex({ "courseId": 1, "actionTime": -1 });
db.learning_behaviors.createIndex({ "actionType": 1, "actionTime": -1 });

// 为用户学习统计集合创建索引
db.user_learning_stats.createIndex({ "userId": 1, "statDate": -1 });

// 为课程评论集合创建索引
db.course_comments.createIndex({ "courseId": 1, "createTime": -1 });
db.course_comments.createIndex({ "userId": 1, "createTime": -1 });

// 为聊天消息集合创建索引
db.chat_messages.createIndex({ "roomId": 1, "createTime": -1 });
db.chat_messages.createIndex({ "userId": 1, "createTime": -1 });

// 为推荐记录集合创建索引
db.recommendations.createIndex({ "userId": 1, "createTime": -1 });
db.recommendations.createIndex({ "algorithmType": 1, "createTime": -1 });

print("MongoDB collections and indexes created successfully!");
