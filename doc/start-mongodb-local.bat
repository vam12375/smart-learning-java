@echo off
echo Smart Learning Platform - MongoDB 启动脚本
echo ==========================================
echo.

REM 检查MongoDB是否已安装
where mongod >nul 2>nul
if %errorlevel% neq 0 (
    echo [错误] 未找到MongoDB，请先安装MongoDB 8.0
    echo.
    echo 安装步骤：
    echo 1. 访问 https://www.mongodb.com/try/download/community
    echo 2. 下载MongoDB Community Server 8.0
    echo 3. 安装后将bin目录添加到PATH环境变量
    echo.
    pause
    exit /b 1
)

echo [信息] 检测到MongoDB已安装
mongod --version | findstr "db version"
echo.

REM 创建数据目录
set DATA_DIR=%~dp0mongodb-data
set LOG_DIR=%~dp0mongodb-logs

if not exist "%DATA_DIR%" (
    echo [信息] 创建数据目录: %DATA_DIR%
    mkdir "%DATA_DIR%"
)

if not exist "%LOG_DIR%" (
    echo [信息] 创建日志目录: %LOG_DIR%
    mkdir "%LOG_DIR%"
)

REM 检查端口是否被占用
netstat -an | findstr ":27017" >nul
if %errorlevel% equ 0 (
    echo [警告] 端口27017已被占用，MongoDB可能已在运行
    echo.
    echo 如需重启MongoDB，请先停止现有进程：
    echo taskkill /f /im mongod.exe
    echo.
    pause
    exit /b 1
)

echo [信息] 启动MongoDB服务...
echo 数据目录: %DATA_DIR%
echo 日志目录: %LOG_DIR%
echo 端口: 27017
echo 数据库: smart_learning
echo.

REM 启动MongoDB
start "MongoDB Server" mongod --dbpath "%DATA_DIR%" --logpath "%LOG_DIR%\mongod.log" --port 27017

REM 等待MongoDB启动
echo [信息] 等待MongoDB启动...
timeout /t 5 /nobreak >nul

REM 检查MongoDB是否启动成功
mongosh --eval "db.adminCommand('ismaster')" --quiet >nul 2>nul
if %errorlevel% equ 0 (
    echo [成功] MongoDB启动成功！
    echo.
    echo 连接信息：
    echo - 地址: mongodb://localhost:27017
    echo - 数据库: smart_learning
    echo - 无需认证
    echo.
    echo 管理工具：
    echo - MongoDB Shell: mongosh mongodb://localhost:27017/smart_learning
    echo - MongoDB Compass: mongodb://localhost:27017/smart_learning
    echo.
    
    REM 初始化数据库
    echo [信息] 是否要初始化数据库？(Y/N)
    set /p choice=请选择: 
    if /i "%choice%"=="Y" (
        echo [信息] 正在初始化数据库...
        mongosh mongodb://localhost:27017/smart_learning --file "%~dp0docker\mongodb\init\01-init-collections.js"
        mongosh mongodb://localhost:27017/smart_learning --file "%~dp0docker\mongodb\init\02-sample-data.js"
        mongosh mongodb://localhost:27017/smart_learning --file "%~dp0docker\mongodb\init\03-test-data.js"
        echo [成功] 数据库初始化完成！
    )
    
    echo.
    echo 要停止MongoDB，请运行: stop-mongodb-local.bat
    echo 或者关闭MongoDB Server窗口
    
) else (
    echo [错误] MongoDB启动失败，请检查日志文件：
    echo %LOG_DIR%\mongod.log
)

echo.
pause
