@ECHO OFF
SETLOCAL
ECHO [Generate VO, Mapper...]
CALL .\set-jdk-env.bat
CALL .\mvnw.cmd -t .\toolchains.xml mybatis-generator:generate

echo.
echo Build log checked? Press Enter to close...
set /p dummyVar=""