@echo off
echo Starting Smart Learning Platform Services...

echo.
echo ========================================
echo Checking Prerequisites...
echo ========================================

echo Checking Java...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java 21 and add it to PATH.
    pause
    exit /b 1
)

echo Checking Maven...
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Maven is not installed or not in PATH!
    echo Please install Maven and add it to PATH.
    pause
    exit /b 1
)

echo Checking MySQL...
mysql --version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo WARNING: MySQL command not found. Make sure MySQL is running.
)

echo Checking Redis...
redis-cli ping >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo WARNING: Redis not responding. Starting Redis...
    start "Redis Server" redis-server
    timeout /t 3 >nul
)

echo.
echo ========================================
echo Compiling Projects...
echo ========================================

echo Installing learning-common...
cd learning-common
call mvn clean install -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ERROR: Failed to install learning-common!
    pause
    exit /b 1
)

cd ..
echo.
echo ========================================
echo Starting Services...
echo ========================================

echo Starting User Service on port 8081...
start "User Service" cmd /k "cd /d %CD%\user-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting next service...
timeout /t 10 >nul

echo Starting Course Service on port 8082...
start "Course Service" cmd /k "cd /d %CD%\course-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting next service...
timeout /t 10 >nul

echo Starting Learning Service on port 8083...
start "Learning Service" cmd /k "cd /d %CD%\learning-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting next service...
timeout /t 10 >nul

echo Starting Exam Service on port 8084...
start "Exam Service" cmd /k "cd /d %CD%\exam-service && mvn spring-boot:run"

echo.
echo ========================================
echo All services are starting...
echo ========================================
echo.
echo Please wait for all services to start completely.
echo You can check the status by visiting:
echo.
echo User Service:     http://localhost:8081/actuator/health
echo Course Service:   http://localhost:8082/actuator/health  
echo Learning Service: http://localhost:8083/actuator/health
echo Exam Service:     http://localhost:8084/actuator/health
echo.
echo API Documentation:
echo User Service:     http://localhost:8081/doc.html
echo Course Service:   http://localhost:8082/doc.html
echo Learning Service: http://localhost:8083/doc.html
echo Exam Service:     http://localhost:8084/doc.html
echo.
echo Press any key to open the User Service API documentation...
pause >nul
start http://localhost:8081/doc.html
