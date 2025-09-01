@echo off
echo Smart Learning Platform - MongoDB 停止脚本
echo ==========================================
echo.

REM 检查MongoDB进程
tasklist | findstr "mongod.exe" >nul
if %errorlevel% neq 0 (
    echo [信息] MongoDB进程未运行
    echo.
    pause
    exit /b 0
)

echo [信息] 检测到MongoDB进程正在运行
echo.

REM 尝试优雅关闭
echo [信息] 尝试优雅关闭MongoDB...
mongosh --eval "db.adminCommand('shutdown')" --quiet >nul 2>nul

REM 等待进程结束
timeout /t 3 /nobreak >nul

REM 检查进程是否已结束
tasklist | findstr "mongod.exe" >nul
if %errorlevel% neq 0 (
    echo [成功] MongoDB已成功停止
) else (
    echo [警告] 优雅关闭失败，强制终止进程...
    taskkill /f /im mongod.exe >nul 2>nul
    if %errorlevel% equ 0 (
        echo [成功] MongoDB进程已强制终止
    ) else (
        echo [错误] 无法停止MongoDB进程
    )
)

echo.
echo MongoDB已停止运行
echo.
pause
