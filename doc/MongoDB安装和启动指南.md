# MongoDB 8.0 安装和启动指南

## 前提条件

由于您的系统中没有安装Docker，我们提供两种方式来运行MongoDB：

### 方式一：安装Docker（推荐）

#### 1. 安装Docker Desktop

1. 访问 [Docker官网](https://www.docker.com/products/docker-desktop/)
2. 下载适合您操作系统的Docker Desktop
3. 安装并启动Docker Desktop
4. 验证安装：打开命令行运行 `docker --version`

#### 2. 使用Docker启动MongoDB

安装Docker后，在项目根目录运行：

```bash
# 方式1：使用docker-compose（推荐）
docker-compose up -d mongodb

# 方式2：直接使用docker命令
docker run -d \
  --name smart-learning-mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_DATABASE=smart_learning \
  -v mongodb_data:/data/db \
  -v ./docker/mongodb/init:/docker-entrypoint-initdb.d \
  mongo:8.0
```

### 方式二：直接安装MongoDB

#### 1. 下载MongoDB 8.0

1. 访问 [MongoDB官网](https://www.mongodb.com/try/download/community)
2. 选择MongoDB Community Server 8.0版本
3. 选择您的操作系统（Windows/macOS/Linux）
4. 下载并安装

#### 2. Windows安装步骤

1. 运行下载的`.msi`安装文件
2. 选择"Complete"安装类型
3. 选择"Run service as Network Service user"
4. 不勾选"Install MongoDB Compass"（可选）
5. 完成安装

#### 3. 配置MongoDB

创建MongoDB配置文件 `mongod.conf`：

```yaml
# mongod.conf
storage:
  dbPath: C:\data\db
  journal:
    enabled: true

systemLog:
  destination: file
  logAppend: true
  path: C:\data\log\mongod.log

net:
  port: 27017
  bindIp: 127.0.0.1

security:
  authorization: disabled
```

#### 4. 启动MongoDB服务

```bash
# 创建数据目录
mkdir C:\data\db
mkdir C:\data\log

# 启动MongoDB服务
mongod --config mongod.conf

# 或者直接启动（使用默认配置）
mongod --dbpath C:\data\db
```

## 初始化数据库

### 1. 连接到MongoDB

```bash
# 使用MongoDB Shell连接
mongosh mongodb://localhost:27017/smart_learning
```

### 2. 手动执行初始化脚本

由于没有Docker的自动初始化，需要手动执行初始化脚本：

```javascript
// 在mongosh中执行以下命令

// 1. 切换到smart_learning数据库
use smart_learning

// 2. 创建集合和索引（复制docker/mongodb/init/01-init-collections.js的内容）
db.createCollection('learning_notes');
db.createCollection('learning_behaviors');
db.createCollection('user_learning_stats');
db.createCollection('course_comments');
db.createCollection('chat_messages');
db.createCollection('recommendations');

// 创建索引
db.learning_notes.createIndex({ "userId": 1, "courseId": 1 });
db.learning_notes.createIndex({ "userId": 1, "createTime": -1 });
db.learning_notes.createIndex({ "courseId": 1, "isPublic": 1 });

db.learning_behaviors.createIndex({ "userId": 1, "actionTime": -1 });
db.learning_behaviors.createIndex({ "courseId": 1, "actionTime": -1 });
db.learning_behaviors.createIndex({ "actionType": 1, "actionTime": -1 });

db.user_learning_stats.createIndex({ "userId": 1, "statDate": -1 });

db.course_comments.createIndex({ "courseId": 1, "createTime": -1 });
db.course_comments.createIndex({ "userId": 1, "createTime": -1 });

db.chat_messages.createIndex({ "roomId": 1, "createTime": -1 });
db.chat_messages.createIndex({ "userId": 1, "createTime": -1 });

db.recommendations.createIndex({ "userId": 1, "createTime": -1 });
db.recommendations.createIndex({ "algorithmType": 1, "createTime": -1 });
```

### 3. 插入示例数据

将 `docker/mongodb/init/02-sample-data.js` 和 `docker/mongodb/init/03-test-data.js` 文件中的内容复制到mongosh中执行。

## 验证安装

### 1. 检查MongoDB状态

```bash
# 检查MongoDB进程
tasklist | findstr mongod

# 检查端口占用
netstat -an | findstr 27017
```

### 2. 测试连接

```bash
# 使用mongosh连接
mongosh mongodb://localhost:27017/smart_learning

# 查看数据库
show dbs

# 查看集合
show collections

# 查看示例数据
db.learning_notes.find().limit(5)
```

### 3. 使用MongoDB Compass（可选）

1. 下载并安装 [MongoDB Compass](https://www.mongodb.com/products/compass)
2. 连接字符串：`mongodb://localhost:27017/smart_learning`
3. 浏览数据库和集合

## 启动应用

### 1. 确保MongoDB运行

在启动Spring Boot应用之前，确保MongoDB服务正在运行：

```bash
# 检查MongoDB状态
mongosh --eval "db.adminCommand('ismaster')"
```

### 2. 启动Spring Boot应用

```bash
# 进入learning-service目录
cd learning-service

# 启动应用
mvn spring-boot:run

# 或者使用IDE启动LearningServiceApplication
```

### 3. 测试API

应用启动后，可以测试MongoDB相关的API：

```bash
# 测试保存学习笔记
curl -X POST http://localhost:8080/api/mongo/learning/notes \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "courseId": 1,
    "lessonId": 1,
    "title": "测试笔记",
    "content": "这是一个测试笔记",
    "type": "TEXT",
    "isPublic": true,
    "tags": ["测试", "MongoDB"]
  }'

# 查询用户笔记
curl http://localhost:8080/api/mongo/learning/notes/user/1

# 搜索笔记
curl "http://localhost:8080/api/mongo/learning/notes/search?keyword=测试"
```

## 常见问题解决

### 1. MongoDB启动失败

- 检查端口27017是否被占用
- 确保数据目录存在且有写权限
- 查看MongoDB日志文件

### 2. 连接失败

- 确认MongoDB服务正在运行
- 检查防火墙设置
- 验证连接字符串格式

### 3. 权限问题

- 确保MongoDB数据目录有读写权限
- 检查用户账户权限

### 4. 应用连接MongoDB失败

- 检查application.yml中的MongoDB配置
- 确认MongoDB服务地址和端口
- 查看应用日志中的错误信息

## 生产环境注意事项

1. **安全配置**：启用认证和访问控制
2. **数据备份**：设置定期备份策略
3. **监控告警**：配置MongoDB监控
4. **性能优化**：根据使用情况优化配置
5. **高可用**：考虑副本集或分片配置

## 下一步

1. 安装并启动MongoDB
2. 执行初始化脚本
3. 启动Spring Boot应用
4. 测试MongoDB功能
5. 根据需要调整配置

如果您在安装或配置过程中遇到问题，请查看相关日志文件或联系技术支持。
