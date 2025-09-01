@echo off
echo ========================================
echo 智能学习平台服务启动测试
echo ========================================

echo.
echo 选择启动模式:
echo 1. 本地配置模式 (不使用Nacos)
echo 2. Nacos配置模式 (需要Nacos服务器)
echo.
set /p choice=请选择 (1 或 2): 

if "%choice%"=="1" (
    echo.
    echo 使用本地配置模式启动用户服务...
    echo.
    cd user-service
    mvn spring-boot:run -Dspring.profiles.active=dev -Dspring.cloud.nacos.discovery.enabled=false -Dspring.cloud.nacos.config.enabled=false
) else if "%choice%"=="2" (
    echo.
    echo 检查Nacos服务器状态...
    curl -s http://localhost:8848/nacos/v1/console/health >nul 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: Nacos服务器未运行或无法访问!
        echo 请确保Nacos服务器在 localhost:8848 上运行
        pause
        exit /b 1
    )
    echo Nacos服务器运行正常
    echo.
    echo 使用Nacos配置模式启动用户服务...
    echo.
    cd user-service
    mvn spring-boot:run -Dspring.profiles.active=dev
) else (
    echo 无效选择，退出...
    pause
    exit /b 1
)

echo.
echo 服务启动中...
echo 请等待服务完全启动后测试
echo.
echo 测试地址:
echo - 健康检查: http://localhost:8081/actuator/health
echo - API文档: http://localhost:8081/doc.html
echo.
pause
