@echo off
echo Starting MongoDB for Smart Learning Platform...
echo.

echo Stopping existing MongoDB container...
docker stop smart-learning-mongodb 2>nul
docker rm smart-learning-mongodb 2>nul

echo.
echo Starting MongoDB 8.0 container...
docker-compose up -d mongodb

echo.
echo Waiting for MongoDB to be ready...
timeout /t 10 /nobreak >nul

echo.
echo Checking MongoDB status...
docker logs smart-learning-mongodb --tail 10

echo.
echo MongoDB is starting up...
echo You can connect to MongoDB at: mongodb://localhost:27017/smart_learning
echo Database: smart_learning
echo No authentication required
echo.

echo To view MongoDB logs:
echo docker logs smart-learning-mongodb -f
echo.

echo To connect using MongoDB Compass:
echo Connection String: mongodb://localhost:27017/smart_learning
echo.

echo To stop MongoDB:
echo docker stop smart-learning-mongodb
echo.

pause
