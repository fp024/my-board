@echo off
SETLOCAL
set "WAR_FILE=target\my-board-mds-1.0.0-BUILD-SNAPSHOT.war"

echo Starting web server on Docker Compose...
cd /d "%~dp0"

REM Ensure docker/web-upload directory exists
if not exist "docker\web-upload" (
    echo [Creating docker/web-upload directory...]
    mkdir docker\web-upload
)

if not exist "%WAR_FILE%" (
    echo [Building WAR...]
    CALL .\set-jdk-env.bat
    CALL .\mvnw.cmd -t .\toolchains.xml clean package -DskipTests -Plinux
    if errorlevel 1 (
        echo [ERROR] Build failed.
        pause
        exit /b 1
    )
)

echo.
echo Ensuring database service "oracle-free" is up and healthy...
docker compose up -d --wait --wait-timeout 240 oracle-free
if errorlevel 1 (
    echo [ERROR] oracle-free did not become healthy in time.
    pause
    exit /b 1
)

echo Starting web service "web-tomcat"...
docker compose up -d --wait --wait-timeout 120 web-tomcat
if errorlevel 1 (
    echo [ERROR] Failed to start web-tomcat.
    pause
    exit /b 1
)

echo.
echo Web server is starting up.
echo   - App URL : http://localhost:8080/
echo.
echo Showing web-tomcat logs now. Press Ctrl+C to stop watching logs.
docker compose logs -f web-tomcat
