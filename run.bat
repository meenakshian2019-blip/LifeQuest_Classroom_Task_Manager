@echo off
setlocal
if "%LIFEQUEST_DB_USER%"=="" set LIFEQUEST_DB_USER=root
if "%LIFEQUEST_DB_URL%"=="" set LIFEQUEST_DB_URL=jdbc:mysql://localhost:3306/lifequest?useSSL=false^&serverTimezone=UTC
echo LifeQuest Classroom Manager
 echo Make sure MySQL is running and database.sql has been executed.
echo If your MySQL password differs from the project default, set LIFEQUEST_DB_PASSWORD before running this file.
call mvn clean compile exec:java
if errorlevel 1 pause
endlocal
