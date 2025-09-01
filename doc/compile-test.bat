@echo off
echo Testing compilation for all services...

echo.
echo ========================================
echo Compiling Learning Common...
echo ========================================
cd learning-common
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Learning Common compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compiling User Service...
echo ========================================
cd ..\user-service
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: User Service compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compiling Course Service...
echo ========================================
cd ..\course-service
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Course Service compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compiling Learning Service...
echo ========================================
cd ..\learning-service
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Learning Service compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compiling Exam Service...
echo ========================================
cd ..\exam-service
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERROR: Exam Service compilation failed!
    pause
    exit /b 1
)

cd ..
echo.
echo ========================================
echo All services compiled successfully!
echo ========================================
echo.
echo You can now start the services using:
echo 1. mvn spring-boot:run (in each service directory)
echo 2. Or use the start-services.bat script
echo.
pause
