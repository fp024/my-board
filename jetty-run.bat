@ECHO OFF
SETLOCAL
ECHO [Jetty Run...]
CALL .\set-jdk-env.bat
CALL .\mvnw.cmd -t .\toolchains.xml clean -Pjetty-run jetty:run