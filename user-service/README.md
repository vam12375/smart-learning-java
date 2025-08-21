# 用户服务 (User Service)

用户服务是智能学习平台的核心服务之一，负责用户管理、认证授权等功能。

## 功能特性

### 核心功能
- ✅ 用户注册
- ✅ 用户登录
- ✅ JWT Token认证
- ✅ Token刷新
- ✅ 用户信息管理
- ✅ 密码加密存储
- ✅ Redis缓存优化
- ✅ 用户状态管理

### 技术特性
- Spring Boot 3.2.0
- Spring Security 6.x
- JWT认证机制
- BCrypt密码加密
- Redis缓存
- MyBatis Plus ORM
- MySQL数据库
- Swagger API文档

## API接口

### 认证相关
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `POST /api/user/refresh-token` - 刷新Token
- `POST /api/user/logout` - 用户登出

### 用户管理
- `GET /api/user/profile` - 获取当前用户信息
- `PUT /api/user/profile` - 更新用户信息
- `GET /api/user/{userId}` - 根据ID获取用户信息

### 工具接口
- `GET /api/user/check-username` - 检查用户名是否可用
- `GET /api/user/check-email` - 检查邮箱是否可用

## 快速开始

### 1. 环境准备
```bash
# MySQL 8.0+
# Redis 6.0+
# Java 21+
# Maven 3.8+
```

### 2. 数据库初始化
```sql
-- 执行数据库脚本
source src/main/resources/sql/user_service_schema.sql
```

### 3. 配置文件
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_learning
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password

jwt:
  secret: your-jwt-secret-key
  expiration: 86400  # 24小时
  refresh-expiration: 604800  # 7天
```

### 4. 启动服务
```bash
mvn spring-boot:run
```

### 5. 访问API文档
- Swagger UI: http://localhost:8081/doc.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs

## 使用示例

### 用户注册
```bash
curl -X POST http://localhost:8081/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "nickname": "测试用户"
  }'
```

### 用户登录
```bash
curl -X POST http://localhost:8081/api/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 获取用户信息
```bash
curl -X GET http://localhost:8081/api/user/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "X-User-Id: 1"
```

## 默认用户

系统初始化时会创建以下默认用户：

| 用户名 | 邮箱 | 密码 | 角色 |
|--------|------|------|------|
| admin | admin@smartlearning.com | admin123 | ADMIN |
| student1 | student1@example.com | admin123 | STUDENT |
| teacher1 | teacher1@example.com | admin123 | TEACHER |

## 安全特性

### 密码安全
- 使用BCrypt算法加密存储密码
- 密码强度验证（最少6位）
- 密码确认验证

### Token安全
- JWT Token有效期24小时
- Refresh Token有效期7天
- Token包含用户ID、用户名、角色信息

### 接口安全
- 公开接口：注册、登录、Token刷新
- 受保护接口：需要有效JWT Token
- 角色权限控制

## 缓存策略

### Redis缓存
- 用户信息缓存（30分钟）
- 多键缓存：用户ID、用户名、邮箱
- 自动缓存更新和清理

### 缓存键规则
- 用户信息：`user:info:{userId|username|email}`
- 用户资料：`user:profile:{userId}`

## 监控和日志

### 健康检查
- 端点：`/actuator/health`
- 数据库连接检查
- Redis连接检查

### 日志记录
- 用户注册日志
- 用户登录日志
- 认证失败日志
- 系统异常日志

## 开发指南

### 添加新的认证方式
1. 实现认证接口
2. 添加相应的DTO和VO
3. 更新控制器
4. 添加测试用例

### 扩展用户属性
1. 修改User实体类
2. 更新数据库表结构
3. 修改相关DTO和VO
4. 更新缓存逻辑

## 测试

### 单元测试
```bash
mvn test
```

### 集成测试
```bash
mvn verify
```

## 部署

### Docker部署
```bash
# 构建镜像
docker build -t user-service:1.0.0 .

# 运行容器
docker run -d -p 8081:8081 user-service:1.0.0
```

### Kubernetes部署
```bash
kubectl apply -f k8s/user-service.yaml
```

## 故障排除

### 常见问题
1. **数据库连接失败**：检查数据库配置和网络连接
2. **Redis连接失败**：检查Redis配置和服务状态
3. **JWT Token无效**：检查Token格式和密钥配置
4. **密码验证失败**：检查密码加密算法和配置

### 日志查看
```bash
# 查看应用日志
tail -f logs/user-service.log

# 查看错误日志
grep ERROR logs/user-service.log
```
