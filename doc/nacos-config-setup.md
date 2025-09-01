# Nacos 配置设置指南

## 1. 访问Nacos控制台
- URL: http://localhost:8848/nacos
- 用户名: nacos
- 密码: nacos

## 2. 创建命名空间
1. 进入"命名空间"页面
2. 点击"新建命名空间"
3. 填写信息：
   - 命名空间ID: `smart-learning`
   - 命名空间名: `智能学习平台`
   - 描述: `智能学习平台微服务配置`

## 3. 配置文件创建清单

在"配置管理" -> "配置列表"中，选择"smart-learning"命名空间，创建以下配置：

### 必需配置文件：
1. **common-config.yml** (Group: DEFAULT_GROUP)
2. **user-service.yml** (Group: DEFAULT_GROUP)
3. **user-service-dev.yml** (Group: DEFAULT_GROUP)
4. **course-service.yml** (Group: DEFAULT_GROUP)
5. **course-service-dev.yml** (Group: DEFAULT_GROUP)
6. **learning-service.yml** (Group: DEFAULT_GROUP)
7. **learning-service-dev.yml** (Group: DEFAULT_GROUP)
8. **exam-service.yml** (Group: DEFAULT_GROUP)
9. **exam-service-dev.yml** (Group: DEFAULT_GROUP)

### 配置文件内容
详细的配置文件内容请参考上面提供的YAML配置。

## 4. 配置验证
创建配置后，可以通过以下方式验证：
1. 在Nacos控制台查看配置是否正确创建
2. 启动服务时观察日志，确认配置加载成功
3. 访问服务的 `/actuator/nacos` 端点查看配置信息

## 5. 动态配置刷新
- 所有配置都支持动态刷新
- 修改Nacos中的配置后，服务会自动重新加载
- 可以通过 `@RefreshScope` 注解实现配置的动态刷新

## 6. 服务发现验证
启动服务后，在Nacos控制台的"服务管理" -> "服务列表"中应该能看到：
- user-service
- course-service
- learning-service
- exam-service

## 7. 故障排除
如果服务无法连接到Nacos：
1. 检查Nacos服务是否正常运行
2. 检查网络连接
3. 检查命名空间配置是否正确
4. 查看服务启动日志中的错误信息
