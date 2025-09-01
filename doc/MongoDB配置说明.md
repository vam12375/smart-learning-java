# MongoDB 8.0 配置说明

## 概述

本项目已配置MongoDB 8.0作为NoSQL数据库，用于存储学习笔记、行为分析、统计数据等非结构化数据。MongoDB配置为无密码模式，便于开发和测试。

## 配置详情

### 1. Docker配置

**docker-compose.yml** 中的MongoDB配置：
```yaml
mongodb:
  image: mongo:8.0
  container_name: smart-learning-mongodb
  restart: always
  environment:
    MONGO_INITDB_DATABASE: smart_learning
  ports:
    - "27017:27017"
  volumes:
    - mongodb_data:/data/db
    - ./docker/mongodb/init:/docker-entrypoint-initdb.d
  networks:
    - smart-learning-network
```

### 2. 应用配置

**application.yml** 中的MongoDB连接配置：
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/smart_learning
```

### 3. 数据库结构

#### 集合（Collections）

1. **learning_notes** - 学习笔记
   - 用户学习笔记存储
   - 支持文本、图片、音频类型
   - 公开/私有笔记管理
   - 点赞和标签功能

2. **learning_behaviors** - 学习行为分析
   - 用户学习行为记录
   - 观看视频、做练习、参与讨论等
   - 设备信息和地理位置
   - 学习时长和进度跟踪

3. **user_learning_stats** - 用户学习统计
   - 每日学习统计数据
   - 学习时长、课程数、连续天数
   - 学习目标进度跟踪
   - 排行榜数据支持

4. **course_comments** - 课程评论
   - 课程评价和评论
   - 评分和点赞功能
   - 回复数统计

5. **chat_messages** - 聊天消息
   - 直播间聊天记录
   - 普通消息和系统消息
   - 用户信息和时间戳

6. **recommendations** - 推荐记录
   - AI推荐算法结果
   - 协同过滤和内容推荐
   - 推荐效果跟踪

### 4. 索引配置

系统自动创建以下索引以优化查询性能：

```javascript
// 学习笔记索引
db.learning_notes.createIndex({ "userId": 1, "courseId": 1 });
db.learning_notes.createIndex({ "userId": 1, "createTime": -1 });
db.learning_notes.createIndex({ "courseId": 1, "isPublic": 1 });

// 学习行为索引
db.learning_behaviors.createIndex({ "userId": 1, "actionTime": -1 });
db.learning_behaviors.createIndex({ "courseId": 1, "actionTime": -1 });
db.learning_behaviors.createIndex({ "actionType": 1, "actionTime": -1 });

// 用户统计索引
db.user_learning_stats.createIndex({ "userId": 1, "statDate": -1 });

// 课程评论索引
db.course_comments.createIndex({ "courseId": 1, "createTime": -1 });
db.course_comments.createIndex({ "userId": 1, "createTime": -1 });

// 聊天消息索引
db.chat_messages.createIndex({ "roomId": 1, "createTime": -1 });
db.chat_messages.createIndex({ "userId": 1, "createTime": -1 });

// 推荐记录索引
db.recommendations.createIndex({ "userId": 1, "createTime": -1 });
db.recommendations.createIndex({ "algorithmType": 1, "createTime": -1 });
```

## 启动和使用

### 1. 启动MongoDB

使用提供的启动脚本：
```bash
# Windows
start-mongodb.bat

# 或者使用docker-compose
docker-compose up -d mongodb
```

### 2. 连接信息

- **连接地址**: `mongodb://localhost:27017`
- **数据库名**: `smart_learning`
- **认证**: 无需密码
- **管理工具**: MongoDB Compass

### 3. 验证连接

启动应用后，可以通过以下方式验证MongoDB连接：

1. 查看应用日志中的MongoDB连接信息
2. 使用MongoDB Compass连接并查看数据
3. 调用API接口测试数据操作

## API接口

### 学习笔记相关

- `POST /api/mongo/learning/notes` - 保存学习笔记
- `GET /api/mongo/learning/notes/{id}` - 查询笔记详情
- `GET /api/mongo/learning/notes/user/{userId}` - 查询用户笔记
- `GET /api/mongo/learning/notes/search?keyword=xxx` - 搜索笔记
- `POST /api/mongo/learning/notes/{id}/like` - 点赞笔记

### 学习行为相关

- `POST /api/mongo/learning/behaviors` - 记录学习行为
- `GET /api/mongo/learning/behaviors/user/{userId}` - 查询用户行为
- `GET /api/mongo/learning/behaviors/duration/{userId}` - 统计学习时长

### 学习统计相关

- `POST /api/mongo/learning/stats` - 保存学习统计
- `GET /api/mongo/learning/stats/{userId}?date=2024-01-21` - 查询统计数据
- `GET /api/mongo/learning/stats/ranking` - 查询排行榜
- `POST /api/mongo/learning/stats/generate/{userId}` - 生成每日统计

## 示例数据

系统启动时会自动导入示例数据，包括：

- 学习笔记样例（Java、Spring Boot、React等技术笔记）
- 学习行为记录（观看视频、做练习、参与讨论等）
- 用户学习统计（每日学习时长、课程进度等）
- 课程评论和聊天消息
- AI推荐记录

## 开发指南

### 1. 添加新的文档类

```java
@Data
@Document(collection = "your_collection")
public class YourDocument {
    @Id
    private String id;
    
    @Field("fieldName")
    private String fieldName;
    
    @Field("createTime")
    private LocalDateTime createTime;
}
```

### 2. 创建Repository接口

```java
@Repository
public interface YourRepository extends MongoRepository<YourDocument, String> {
    List<YourDocument> findByFieldName(String fieldName);
    
    @Query("{'fieldName': ?0}")
    List<YourDocument> customQuery(String value);
}
```

### 3. 服务层使用

```java
@Service
@RequiredArgsConstructor
public class YourService {
    private final YourRepository repository;
    
    public YourDocument save(YourDocument doc) {
        return repository.save(doc);
    }
}
```

## 监控和维护

### 1. 查看MongoDB状态

```bash
# 查看容器状态
docker ps | grep mongodb

# 查看日志
docker logs smart-learning-mongodb -f

# 进入MongoDB shell
docker exec -it smart-learning-mongodb mongosh
```

### 2. 数据备份

```bash
# 备份数据库
docker exec smart-learning-mongodb mongodump --db smart_learning --out /backup

# 恢复数据库
docker exec smart-learning-mongodb mongorestore --db smart_learning /backup/smart_learning
```

### 3. 性能监控

- 使用MongoDB Compass查看查询性能
- 监控索引使用情况
- 定期检查慢查询日志

## 注意事项

1. **生产环境安全**: 生产环境中应启用认证和访问控制
2. **数据备份**: 定期备份重要数据
3. **索引优化**: 根据查询模式优化索引
4. **连接池**: 合理配置连接池大小
5. **监控告警**: 设置MongoDB监控和告警机制

## 故障排除

### 常见问题

1. **连接失败**: 检查MongoDB容器是否正常运行
2. **数据不同步**: 检查时区设置和时间转换
3. **查询慢**: 检查索引是否正确创建
4. **内存不足**: 调整MongoDB内存配置

### 日志查看

```bash
# 应用日志
tail -f logs/learning-service.log

# MongoDB日志
docker logs smart-learning-mongodb --tail 100
```
