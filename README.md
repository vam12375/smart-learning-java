# 智能学习管理系统 - 后端服务

基于Spring Boot 3.2 + Spring Cloud 2023 + Java 21的微服务架构在线教育平台。

## 📋 项目概述

智能学习管理系统是一个现代化的在线教育平台，采用微服务架构设计，集成AI智能推荐、实时音视频、大数据分析等技术，为学生、教师和管理员提供完整的在线学习解决方案。

## 🏗️ 系统架构

### 微服务模块

```
smart-learning-platform/
├── learning-common/          # 公共模块 - 通用工具类、实体类、常量等
├── learning-gateway/         # API网关 - 路由、限流、认证
├── user-service/            # 用户服务 - 用户管理、认证授权
├── course-service/          # 课程服务 - 课程管理、课时管理
├── learning-service/        # 学习服务 - 学习记录、进度跟踪、笔记管理
├── exam-service/           # 考试服务 - 题库管理、考试管理、成绩统计
├── live-service/           # 直播服务 - 直播管理、实时通信、录制回放
├── ai-service/             # AI服务 - 推荐算法、数据分析、智能问答
└── analytics-service/      # 数据分析服务 - 数据统计、报表生成、业务分析
```

### 技术栈

- **后端框架**: Spring Boot 3.2, Spring Cloud 2023
- **开发语言**: Java 21
- **服务注册**: Nacos 3.0.2
- **API网关**: Spring Cloud Gateway
- **数据库**: MySQL 8.0, MongoDB 8.0
- **缓存**: Redis 7.0, Redisson
- **搜索引擎**: Elasticsearch 8.11
- **消息队列**: RabbitMQ 3.12
- **对象存储**: MinIO
- **容器化**: Docker, Kubernetes

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. 克隆项目

```bash
git clone <repository-url>
cd smart-learning-platform/smart-learning-java
```

### 2. 启动基础设施

使用Docker Compose启动所需的基础设施服务：

```bash
# 启动所有基础设施服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f [service-name]
```

### 3. 服务访问地址

启动完成后，可以通过以下地址访问各个服务：

| 服务            | 地址                          | 用户名/密码            |
|---------------|-----------------------------|-------------------|
| Nacos控制台      | http://localhost:8848/nacos | nacos/nacos       |
| MySQL数据库      | localhost:3306              | root/admin        |
| Redis         | localhost:6379              | - /               |
| MongoDB       | localhost:27017             | / |
| Elasticsearch | http://localhost:9200       | -                 |
| Kibana        | http://localhost:5601       | -                 |
| RabbitMQ管理界面  | http://localhost:15672      | admin/admin123456 |
| MinIO控制台      | http://localhost:9001       | admin/admin123456 |

### 4. 编译项目

```bash
# 编译所有模块
mvn clean compile

# 打包所有模块
mvn clean package -DskipTests
```

### 5. 启动微服务

按以下顺序启动各个微服务：

```bash
# 1. 启动网关服务
cd learning-gateway
mvn spring-boot:run

# 2. 启动用户服务
cd ../user-service
mvn spring-boot:run

# 3. 启动课程服务
cd ../course-service
mvn spring-boot:run

# 4. 启动其他服务...
```

### 6. 验证服务

访问Nacos控制台 http://localhost:8848/nacos 查看服务注册情况。

## 📊 服务端口分配

| 服务                | 端口   | 描述     |
|-------------------|------|--------|
| learning-gateway  | 8080 | API网关  |
| user-service      | 8081 | 用户服务   |
| course-service    | 8082 | 课程服务   |
| learning-service  | 8083 | 学习服务   |
| exam-service      | 8084 | 考试服务   |
| live-service      | 8085 | 直播服务   |
| ai-service        | 8086 | AI服务   |
| analytics-service | 8087 | 数据分析服务 |

## 🔧 开发指南

### 代码结构

每个微服务模块都遵循以下包结构：

```
src/main/java/com/smartlearning/[service]/
├── controller/          # 控制器层
├── service/            # 业务逻辑层
├── mapper/             # 数据访问层
├── entity/             # 实体类
├── dto/                # 数据传输对象
├── vo/                 # 视图对象
├── config/             # 配置类
└── [Service]Application.java  # 启动类
```

### 配置文件

- `application.yml`: 主配置文件
- `application-dev.yml`: 开发环境配置
- `application-prod.yml`: 生产环境配置

### API文档

启动服务后，可以通过以下地址访问API文档：

- 网关聚合文档: http://localhost:8080/doc.html
- 各服务文档: http://localhost:[port]/doc.html

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn surefire-report:report
```

## 📦 部署

### Docker部署

```bash
# 构建镜像
mvn clean package docker:build

# 运行容器
docker-compose -f docker-compose.prod.yml up -d
```

### Kubernetes部署

```bash
# 应用配置
kubectl apply -f k8s/

# 查看部署状态
kubectl get pods -n smart-learning
```

## 🔍 监控

- **健康检查**: http://localhost:[port]/actuator/health
- **指标监控**: http://localhost:[port]/actuator/metrics
- **服务信息**: http://localhost:[port]/actuator/info

## 📝 开发规范

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 统一异常处理
- 统一响应格式

### Git提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- 项目维护者: 青柠檬
- 邮箱: 2898191344@qq.com
- 项目地址: https://github.com/vam12375/smart-learning-java


